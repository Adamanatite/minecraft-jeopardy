package me.adamm.updatedjeopardy.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.files.ImageConfig;

/**
 * Represents the state of the Jeopardy game on the server
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class Game {
	/* Number of questions answered so far in game */
	private int noAnswered;
	/* Players playing the game */
	private JPlayer[] players;
	/* Players who can answer the current question */
	private Set<JPlayer> playersInQuestion;
	/* The player currently buzzed in */
	private JPlayer playerBuzzed;
	/* The current question being asked */
	private Question currentQuestion;
	/** A list of all questions on the board */
	Question[][] questions;
	
	/* Trackers for if the game is ongoing, buzzers are on and it is final jeopardy */
	private boolean onGoing;
	private boolean buzzersOn;
	private boolean isFinal;
	
	/* Player in control of the board (needed for daily double) */
	private JPlayer playerInControl;
	
	/* Maximum text size of a question */
	int maxCharLength;
	int maxQuestionLines;
	
	/* Co-ordinates used to track board position and direction */
	private String direction;
	private int offset;
	private int coverOffset;
	private World world;
	private int x;
	private int y;
	private int z;
	
	/* Storage for the final jeopardy question */
	private HashMap<JPlayer, Integer> finalBets;
	private HashMap<JPlayer, String> finalAnswers;
	
	/* Question hologram, player holograms and location of buzzers */
	Hologram qHolo;
	Hologram[] pHolos;
	Location[] buzzers;
	
	/* Reference to the main class*/
	private Main plugin;
	
	/**
	 * Creates a new Game object
	 * 
	 * @param plugin A reference to the main class
	 */
	public Game(Main plugin) {
		// Assign variables from config or as null
		this.players = new JPlayer[3];
		this.playersInQuestion = new HashSet<JPlayer>();
		this.playerBuzzed = null;
		this.currentQuestion = null;
		this.questions = new Question[6][5];
		this.plugin = plugin;
		this.onGoing = false;
		this.buzzersOn = false;
		this.playerInControl = null;
		this.noAnswered = 0;
		this.maxCharLength = plugin.getConfig().getInt("max_char_length");
		this.maxQuestionLines = plugin.getConfig().getInt("max_question_lines");
		this.isFinal = false;
		
		this.finalBets = new HashMap<JPlayer, Integer>();
		this.finalAnswers = new HashMap<JPlayer, String>();
		
		this.direction = plugin.getConfig().getString("board_top_left.horizontal");
		this.offset = plugin.getConfig().getInt("board_top_left.out_offset");
		this.coverOffset = plugin.getConfig().getInt("board_top_left.cover_offset");
		this.world = Bukkit.getServer().getWorld(plugin.getConfig().getString("board_top_left.world"));
		this.x = plugin.getConfig().getInt("board_top_left.x");
		this.y = plugin.getConfig().getInt("board_top_left.y");
		this.z = plugin.getConfig().getInt("board_top_left.z");
		
		this.qHolo = this.loadHologram("question");
		this.pHolos = new Hologram[3];
		this.buzzers = new Location[3];
		
		// Set up holograms and buzzer locations for each player
		for(int i = 0; i < pHolos.length; i++) {
			// Hologram
			this.pHolos[i] = this.loadHologram("player" + (i + 1));
			DHAPI.addHologramLine(this.pHolos[i], Utils.chat("$0"));
			// Buzzer location
			int bx = plugin.getConfig().getInt("buzzer.player" + (i + 1) + ".x");
			int by = plugin.getConfig().getInt("buzzer.player" + (i + 1) + ".y");
			int bz = plugin.getConfig().getInt("buzzer.player" + (i + 1) + ".z");
			this.buzzers[i] = new Location(this.world, bx, by, bz);
		}
	}

	/**
	 * Creates and returns a hologram of the given name from the config
	 * 
	 * @param name The name of the hologram in the config
	 * @return h The created hologram
	 */
	private Hologram loadHologram(String name) {
		double x = plugin.getConfig().getDouble("hologram." + name + ".x");
		double y = plugin.getConfig().getDouble("hologram." + name + ".y");
		double z = plugin.getConfig().getDouble("hologram." + name + ".z");
		Location l = new Location(this.world, x, y, z);
		
		Hologram h = DHAPI.createHologram(name, l);
		return h;
		
	}
	
	/**
	 * Moves a hologram up by the given number of blocks (used in picture round questions)
	 * 
	 * @param upBlocks the number of blocks to move the hologram up by
	 */
	private void moveHologram(int upBlocks) {
		double x = plugin.getConfig().getDouble("hologram.question.x");
		double y = plugin.getConfig().getDouble("hologram.question.y");
		double z = plugin.getConfig().getDouble("hologram.question.z");
		Location l = new Location(this.world, x, y+upBlocks, z);
		DHAPI.moveHologram(qHolo, l);
	}
	
	/**
	 * Getter method for if the game is ongoing
	 * 
	 * @return onGoing Is the game ongoing
	 */
	public boolean isOngoing() {
		return this.onGoing;
	}
	
	/**
	 * Check if a given jeopardy player is in the game
	 * 
	 * @return isInGame if the jeopardy player is in the game or not
	 */
	public boolean isInGame(JPlayer j) {
		return (players[0].equals(j) || players[1].equals(j) || players[2].equals(j));
	}
	
	/**
	 * Gets the jeopardy player object of the player with given UUID if it exists
	 * 
	 * @param uuid The UUID of the requested player
	 * @return j The player if they exist in the game, or null if not
	 */
	public JPlayer getPlayer(UUID uuid) {
		
		for(JPlayer j : this.players) {
			if(j != null && j.getUuid().equals(uuid)) {
				return j;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the jeopardy player object of the player with given username if it exists
	 * 
	 * @param name The username of the requested player
	 * @return j The player if they exist in the game, or null if not
	 */
	public JPlayer getPlayerByName(String name) {
		
		for(JPlayer j : this.players) {
			if(j != null && j.getName().equalsIgnoreCase(name)) {
				return j;
			}
		}
		
		return null;
	}
	
	/**
	 * Getter method for the player currently buzzed in
	 * 
	 * @return playerBuzzed The player currently buzzed in
	 */
	public JPlayer getPlayerBuzzed() {
		return this.playerBuzzed;
	}

	/**
	 * Setter method for the player currently buzzed in
	 * 
	 * @param playerBuzzed The player to be set to buzzed in
	 */
	public void setPlayerBuzzed(JPlayer playerBuzzed) {
		this.playerBuzzed = playerBuzzed;
		this.buzzersOn = false;
	}

	/**
	 * Getter method for the players currently in the question
	 * 
	 * @return playersInQuestion The players currently in the question
	 */
	public Set<JPlayer> getPlayersInQuestion(){
		return this.playersInQuestion;
	}
	
	/**
	 * Getter method for the current question
	 * 
	 * @return currentQuestion The current question
	 */
	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	/**
	 * Setter method for the current question
	 * 
	 * @param currentQuestion The current question to be set
	 */
	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}
	
	/**
	 * Adds a player to the current game if there is space
	 * 
	 * @param p The player to add to the game
	 * @return wasAdded If the player was added to the game successfully
	 */
	public boolean addPlayer(Player p) {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i] == null) {
				// If there is space, create new JPlayer object and add the player in the current position
				JPlayer j = new JPlayer(p, pHolos[i], buzzers[i], this.plugin); 
				this.players[i] = j;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes a player from the current game if they are in it
	 * 
	 * @param uuid The UUID of the player to remove from the game
	 * @return wasRemoved If the player was removed from the game successfully
	 */
	public boolean removePlayer(UUID uuid) {
		int l = this.players.length;
		for(int i = 0; i < l; i++) {
			if(this.players[i] != null && this.players[i].getUuid().equals(uuid)) {
				this.players[i] = null;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Getter method of the list of players in the game (as strings)
	 * 
	 * @return l The list of players in the game as strings
	 */
	public List<String> getPlayers(){
		List<String> l = new ArrayList<String>();
		
		for(int i = 0; i < this.players.length; i++) {
			if(!(this.players[i] == null)) {
			l.add(this.players[i].getName());
			}
			else {
				l.add("<Empty>");
			}
		}
		
		return l;
		
	}
	
	/**
	 * Resets all player scores
	 */
	public void resetScores() {
		for(JPlayer j : this.players) {
			if(j != null) {
				j.resetScore();
			}
		}
	}
	
	/**
	 * Loads the game board of the given name from the config
	 * 
	 * @param board_name The name of the board in the config
	 */
	public void loadBoard(String board_name) {
		
		if(!(plugin.getConfig().contains(board_name))) return;
		
		// Check if the board is a first or second round board for question point values
		final int worth_mul = plugin.getConfig().getInt(board_name + ".cat_1.1.Worth");
		
		// Load category data
		for(int c = 1; c < 7; c++) {
			
			// Get category name
			String cname = plugin.getConfig().getString(board_name + ".cat_" + c + ".Name");
			
			final int c_final = c;
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    @Override
			    public void run() {
			        Location l;
					// Get sign location
					if(direction.equals("x")) {
						l = new Location(world, x+c_final-1, y+1, z+(offset*2));
					} else if(direction.equals("-x")) {
						l = new Location(world, x-c_final+1, y+1, z+(offset*2));	
					} else if(direction.equals("z")) {
						l = new Location(world, x+(offset*2), y+1, z+c_final-1);	
					} else {
						l = new Location(world, x+(offset*2), y+1, z-c_final+1);	
					}
			    	
					// Create sign
					l.getBlock().setType(Material.CRIMSON_WALL_SIGN);
					Sign sign = (Sign) l.getBlock().getState();
					WallSign bd = (WallSign) sign.getBlockData();
					bd.setFacing(BlockFace.NORTH);
					sign.setBlockData(bd);
					
					// Write question on sign
					sign.setLine(2, " ");
					
					if(cname.length() > 15) {
						int index = cname.substring(0, 15).lastIndexOf(' ');
						sign.setLine(1, cname.substring(0, index));
						sign.setLine(2, cname.substring(index+1));
					}
					else {
						sign.setLine(1, cname);
					}
					sign.setGlowingText(true);
					sign.update();
					
					// For each question of a category
					for(int i = 0; i < 5; i++) {
						
						// Get correct block for question item to be on
						Block b;
						if(direction.equals("x")) {			            
							b = world.getBlockAt((x + c_final - 1), (y-i), z);
						} else if(direction.equals("-x")) {
							b = world.getBlockAt((x - c_final + 1), (y-i), z);

						} else if(direction.equals("z")) {
							b = world.getBlockAt(x, (y-i), (z + c_final - 1));	
						} else {
							b = world.getBlockAt(x, (y-i), (z - c_final + 1));	
						}
						
						// Find correct block face and spawn named pickaxe in item frame
				        for (Entity entity : b.getWorld().getNearbyEntities(b.getLocation(), 2, 2, 2)) {
				            if (entity instanceof ItemFrame && entity.getLocation().getBlock().getRelative(((ItemFrame) entity).getAttachedFace()).equals(b)) {
				                ItemFrame itemFrame = (ItemFrame) entity;
				                ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
				                ItemMeta itemMeta = pickaxe.getItemMeta();
				                itemMeta.setDisplayName(String.valueOf((i + 1) * worth_mul));
				                pickaxe.setItemMeta(itemMeta);
				                itemFrame.setItem(pickaxe);
				            }
				            
				        }
					}
					
					// Play sound and give players category title
					l.getWorld().playSound(l, Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
			        for(Player p : Bukkit.getOnlinePlayers()) {
			        	if(p.getWorld().equals(world)) {
			        	p.sendTitle(cname, "", 1, 20, 1);
			        	}
			        } 
			    }
			}, 40L * c);
			
			// Populate array with question information for current category
			for(int q = 1; q < 6; q++) {

				String qname = plugin.getConfig().getString(board_name + ".cat_" + c + "." + q + ".Question");
				String aname = plugin.getConfig().getString(board_name + ".cat_" + c + "." + q + ".Answer");
				int worth = plugin.getConfig().getInt(board_name + ".cat_" + c + "." + q + ".Worth");
				boolean daily_double = plugin.getConfig().contains(board_name + ".cat_" + c + "." + q + ".daily_double");
				Question qu;
				if(plugin.getConfig().contains(board_name + ".cat_" + c + "." + q + ".Image")) {
					String image = plugin.getConfig().getString(board_name + ".cat_" + c + "." + q + ".Image");
					qu = new Question(qname, aname, cname, worth, daily_double, image);
				}
				else {
					qu = new Question(qname, aname, cname, worth, daily_double);					
				}
				this.questions[c-1][q-1] = qu;	
				
			}
		}
		
		// Reset number of questions answered and begin the game
		this.noAnswered = 0;
		this.startGame();
		
	}
	
	/**
	 * Sets the game to start, if it is ready to be started
	 * 
	 * @return started If the game was successfully started
	 */
	public boolean startGame() {
		
		// Ensure all 3 players are in game
		for(JPlayer j : this.players) {
			if(j == null) {
				return false;
			}
		}
		
		// Ensure questions are loaded
		if (this.questions[0][0] == null){
			return false;
		}
		
		// Set up variables for new board
		this.onGoing = true;
		this.playerInControl = this.players[0];
		this.buzzersOn = false;
		return true;
	}
	
	/**
	 * Sets the game to stopped
	 */
	public void stopGame() {
		this.onGoing = false;
	}

	/**
	 * Resets all game information, starting a new game
	 */
	public void resetGame() {
		this.resetScores();
		this.players = new JPlayer[3];
		this.playersInQuestion = new HashSet<JPlayer>();
		this.playerBuzzed = null;
		this.currentQuestion = null;
		this.questions = new Question[6][5];
		this.onGoing = false;
		this.playerInControl = null;
		this.noAnswered = 0;
		this.isFinal = false;
		
		this.finalBets.clear();
		this.finalAnswers.clear();
	}
	
	/**
	 * Gets the question of the given index
	 * 
	 * @param cIndex the index of the category (1-6)
	 * @param qIndex the index of the question (1-5)
	 * @return question The question at the given index
	 */
	private Question get_question(int cindex, int qindex) {
		return this.questions[cindex-1][qindex-1];
	}
	
	/**
	 * Asks the question of the given index in the game
	 * 
	 * @param cIndex the index of the category (1-6)
	 * @param qIndex the index of the question (1-5)
	 */
	public void askQuestion(int cindex, int qindex) {
		
		if(!(this.onGoing)) {return;}
		
		// Set as current question
		Question q = get_question(cindex, qindex);
		this.currentQuestion = q;
		
		// Daily double question
		if(q.isDailyDouble()) {
			// Ensure current player in control is the only one who can answer the question
			this.buzzersOn = false;
			this.playerBuzzed = this.playerInControl;
			this.playerBuzzed.buzzIn();
			this.playersInQuestion.add(this.playerInControl);
			// Show daily double information on holograms and begin accepting bet
			DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("DAILY DOUBLE:")));
			this.world.playSound(qHolo.getLocation(), Sound.BLOCK_BELL_USE, 1.0F, 1.0F);
			int s = this.playerInControl.getScore();
			String scoreString = Utils.getScoreString(Math.max(q.getWorth(), s));
			coverScreen(Material.BLUE_CONCRETE);
			DHAPI.addHologramLine(qHolo, Utils.chat("Enter an amount from $0 to " + scoreString));
			DHAPI.addHologramLine(qHolo, Utils.chat("A correct answer will earn you this amount"));
			DHAPI.addHologramLine(qHolo, Utils.chat("An incorrect answer will lose you this amount"));
			DHAPI.addHologramLine(qHolo, Utils.chat("Place a bet using &l/jbet <amount>"));
			
		}
		else {
			// If normal question, add all players and show question
			for(JPlayer j : this.players) {
				this.playersInQuestion.add(j);
			}
			askCurrentQuestion();
		}
	}
	
	/**
	 * Asks for the bets in the final jeopardy
	 */
	public void askFinalBets() {
		this.isFinal = true;
		
		coverScreen(Material.BLUE_CONCRETE);
		String fCategory = plugin.getConfig().getString("final.Category");
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));
		DHAPI.addHologramLine(qHolo, Utils.chat("Category: &l" + fCategory));
		DHAPI.addHologramLine(qHolo, Utils.chat("Bet up to how much you have (or $1000 if you have less)"));
		DHAPI.addHologramLine(qHolo, Utils.chat("An incorrect answer will lose you this amount"));
		DHAPI.addHologramLine(qHolo, Utils.chat("Place a bet using &l/jbet <amount>"));
	}
	
	/**
	 * Gets the bet for the given player (for daily double or final jeopardy)
	 */
	public void getBet(int bet, JPlayer j) {
		
		// Daily double case
		if(this.currentQuestion != null && this.currentQuestion.isDailyDouble()) {
			this.currentQuestion.setWorth(bet);
			askCurrentQuestion();
			return;
		}
		
		// Final jeopardy case
		if(!this.finalBets.containsKey(j)) {
			this.finalBets.put(j, bet);
		}
		
		// Start final jeopardy if all players have bet
		if(this.finalBets.size() == this.players.length) {
			askFinalQuestion();
		}
		
	}
	
	/**
	 * Shows the final jeopardy question on the question hologram
	 */
	public void askFinalQuestion() {
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));

		String qString = plugin.getConfig().getString("final.Question");
		for(int i = 0; i < this.maxQuestionLines; i++) {
			if(qString.length() < this.maxCharLength) {
				DHAPI.addHologramLine(qHolo, qString);
				break;
			}
			int index = qString.substring(0, this.maxCharLength).lastIndexOf(' ');
			DHAPI.addHologramLine(qHolo, qString.substring(0, index));
			qString = qString.substring(index+1);
		}
		DHAPI.addHologramLine(qHolo, Utils.chat("Use &l/jfinal <answer>"));
		DHAPI.addHologramLine(qHolo, Utils.chat("&4ONLY YOUR FIRST ANSWER IS ACCEPTED"));
	}
	
	/**
	 * Accepts the given players final answer for the final jeopardy
	 * 
	 * @param ans The given answer of the player
	 * @param j The player object of the player giving the answer
	 */
	public void getFinalAnswer(String ans, JPlayer j) {
		
		// Only accept first answer
		if(!this.finalAnswers.containsKey(j)) {
			this.finalAnswers.put(j, ans);
		}
		
		// Show that all players have locked in their answer
		if(this.finalAnswers.size() == this.players.length) {		
			Bukkit.broadcastMessage(Utils.chat("&aAll players have locked in their answer!"));
			this.world.playSound(qHolo.getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1.0F, 1.0F);
			
			// Re-add all lines except last and replace last line
			DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));
			String qString = plugin.getConfig().getString("final.Question");
			for(int i = 0; i < this.maxQuestionLines; i++) {
				if(qString.length() < this.maxCharLength) {
					DHAPI.addHologramLine(qHolo, qString);
					break;
				}
				int index = qString.substring(0, this.maxCharLength).lastIndexOf(' ');
				DHAPI.addHologramLine(qHolo, qString.substring(0, index));
				qString = qString.substring(index+1);
			}
			DHAPI.addHologramLine(qHolo, Utils.chat("&aAll players have locked in their answer!"));
		}
	}
	
	/**
	 * Show the next players answer to the final jeopardy
	 */
	public void showFinalAnswer() {
			
		// Get player with the minimum score who has not had their answer shown
		int score = Integer.MAX_VALUE;
		for(JPlayer j: this.finalBets.keySet()) {
			if(j.getScore() < score) {
				this.playerBuzzed = j;
				score = j.getScore();
			}
		}
		// Show players answer on their hologram and in chat
		this.playerBuzzed.buzzIn();
		Bukkit.broadcastMessage(Utils.chat("&b" + this.playerBuzzed.getName() + " has answered: &l" + this.finalAnswers.get(this.playerBuzzed)));
		for(JPlayer j : this.players) {
			if (j.equals(this.playerBuzzed)){
				j.setHologram(Utils.chat("Answer: &l" + this.finalAnswers.get(this.playerBuzzed)));
			} else {
				j.setHologram(Utils.chat(""));
			}
		}
	}
	
	/**
	 * Gets a player in the game by their game position
	 * 
	 * @param i The given index
	 * @return j The player object at the index, if they exist
	 */
	public JPlayer getPlayerByIndex(int i) {
		// Ensure index is in bounds
		if(i < 0 || i > this.players.length) {
			return null;
		}
		return this.players[i];
	}
	
	/**
	 * Answers a players final jeopardy (for the host to use)
	 * 
	 * @param wasRight If the player was correct
	 */
	public void answerFinalQuestion(boolean wasRight) {
		
		if(!(this.onGoing) || this.playerBuzzed == null) {return;}
		
		// Add or remove points depending on if answer was correct
		int bet = this.finalBets.getOrDefault(this.playerBuzzed, 0);
		if(wasRight) {
			this.playerBuzzed.addScore(bet);
			Bukkit.broadcastMessage(Utils.chat("&a" + this.playerBuzzed.getName() + " just won " + Utils.getScoreString(bet) + "!"));
		} else {
				this.playerBuzzed.removeScore(bet);
				Bukkit.broadcastMessage(Utils.chat("&c" + this.playerBuzzed.getName() + " just lost " + Utils.getScoreString(bet)));
		}	
		
		// Set all other players holograms to normal
		for(JPlayer j : this.players) {
			if (!(j.equals(this.playerBuzzed))){
				j.updateHologram();
			}
		}
		
		// Remove player from question
		this.finalBets.remove(this.playerBuzzed);
		this.playerBuzzed.buzzOut();
		this.finalAnswers.remove(this.playerBuzzed);
		
		// Finish game if all players have answered
		if(this.finalBets.isEmpty()) {
			Bukkit.broadcastMessage(Utils.chat("&bJeopardy is over!"));
			this.revealAnswer();
			this.isFinal = false;
			return;
		}
		
	}
	
	/**
	 * Getter method for if the game is on the final jeopardy
	 * 
	 * @return isFinal If the game is on the final jeopardy
	 */
	public boolean isFinal() {
		return this.isFinal;
	}
	
	/**
	 * Asks the current question on hologram (or loads picture)
	 */
	public void askCurrentQuestion() {
		this.buzzersOn = false;
		Question q = this.currentQuestion;
		
		// Load question text
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7" + q.getCategory() + " for " + Utils.getScoreString(q.getWorth()))));
		
		String qString = q.getQuestion();
		for(int i = 0; i < this.maxQuestionLines; i++) {
			if(qString.length() < this.maxCharLength) {
				DHAPI.addHologramLine(qHolo, qString);
				break;
			}
			int index = qString.substring(0, this.maxCharLength).lastIndexOf(' ');
			DHAPI.addHologramLine(qHolo, qString.substring(0, index));
			qString = qString.substring(index+1);
		}
		
		// Cover board (with image or blue concrete)
		if(this.currentQuestion.isImageQuestion()) {
			
			String template = this.currentQuestion.getImageTemplate();
			
			if(ImageConfig.get().contains(template)) {
				// Move hologram up
				moveHologram(2);
				
				// Load image template
				List<String> blocks = ImageConfig.get().getStringList(template);
				loadImage(blocks);
				
			} else {
				Bukkit.broadcastMessage("Couldn't find image " + template);
				coverScreen(Material.BLUE_CONCRETE);
			}
			
		} else {
			coverScreen(Material.BLUE_CONCRETE);			
		}

	}
	
	/**
	 * Getter method for if buzzers are on
	 * @return buzzersOn If the buzzers are on
	 */
	public boolean buzzersOn() {
		return this.buzzersOn;
	}
	
	/**
	 * Activates buzzers
	 */
	public void activateBuzzers() {
		// Show final jeopardy answer if all players have answered the final jeopardy
		if(this.isFinal && !this.finalAnswers.isEmpty()) {
				this.showFinalAnswer();
				return;
		}
		this.buzzersOn = true;
	}
	
	/**
	 * Answers a given question for the player (for the host to use)
	 * @param wasRight If the player answered correctly
	 */
	public void answerQuestion(boolean wasRight) {
		// Final jeopardy case
		if(this.isFinal) {
			answerFinalQuestion(wasRight);
			return;
		}
		
		if(!(this.onGoing) || this.playerBuzzed == null || this.currentQuestion == null) {return;}
		
		// Sounds, updating holograms and variables
		if(wasRight) {
			this.world.playSound(this.playerBuzzed.getBuzzerLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0F, 1.0F);
			this.playerBuzzed.addScore(this.currentQuestion.getWorth());
			this.playerInControl = this.playerBuzzed;
			this.playerBuzzed.buzzOut();
			this.playerBuzzed = null;
			this.revealAnswer();
			
		} else {
			this.world.playSound(this.playerBuzzed.getBuzzerLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
			this.playerBuzzed.removeScore(this.currentQuestion.getWorth());
			this.playersInQuestion.remove(this.playerBuzzed);
			this.playerBuzzed.buzzOut();
			this.playerBuzzed = null;
			if(this.playersInQuestion.isEmpty()) {
				this.revealAnswer();
			} else {
				this.activateBuzzers();
			}
		}
		
	}
	
	/**
	 * Reveals the answer to a question (using the hologram)
	 */
	public void revealAnswer() {
		
		if(this.isFinal()) {
			DHAPI.addHologramLine(qHolo, Utils.chat("Answer: " + plugin.getConfig().getString("final.Answer")));
		}
		else {
		if(this.currentQuestion == null) {return;}
		
		// If a player is buzzed in, give them a wrong answer
		if(this.getPlayerBuzzed() != null) {
			this.answerQuestion(false);
		}
		// End question
		for(JPlayer j : this.players) {
			this.playersInQuestion.remove(j);
		}
		
		this.noAnswered++;
		if(this.noAnswered == 30) {
			Bukkit.broadcastMessage(Utils.chat("&bAll questions on this board answered!"));
		}
		else {
			Bukkit.broadcastMessage(Utils.chat("&b&l" + this.playerInControl.getName() + "&r&b, choose another question!"));
		}
		DHAPI.addHologramLine(qHolo, Utils.chat("Answer: " + this.currentQuestion.getAnswer()));
		
		this.currentQuestion = null;
		}
		
		// Remove question from board and clear board after 3 seconds (for time to read answer)
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	DHAPI.setHologramLines(qHolo, Arrays.asList(""));
		        coverScreen(Material.AIR);
		        moveHologram(0);
		    }
		}, 60L);	
	}
	
	/**
	 * Skips the current question
	 */
	public void skipQuestion() {
		
		if(this.currentQuestion == null) {return;}
		
		// Move hologram back to normal position after image question
		if(this.currentQuestion.isImageQuestion()) {
			moveHologram(0);
		}
		
		// Reset variables for no current question
		if(this.getPlayerBuzzed() != null) {
			this.getPlayerBuzzed().buzzOut();
			this.playerBuzzed = null;
		}
		
		for(JPlayer j : this.players) {
			this.playersInQuestion.remove(j);
		}

		this.currentQuestion = null;
		
		// Reset hologram and screen
		DHAPI.setHologramLines(qHolo, Arrays.asList(""));
		coverScreen(Material.AIR);	
	}
	
	
	/**
	 * Covers the screen with the given material to make question hologram more legible (can be Material.AIR to uncover the screen)
	 * 
	 * @param m the material to cover the screen with
	 */
	public void coverScreen(Material m) {
		Location l;
		for(int i = -3; i < 9; i++) {
			for(int j = -1; j < 5; j++) {
				
				if(direction.equals("x")) {
					l = new Location(world, x+i, y-j, z+(offset*coverOffset));
				} else if(direction.equals("-x")) {
					l = new Location(world, x-i, y-j, z+(offset*coverOffset));	
				} else if(direction.equals("z")) {
					l = new Location(world, x+(offset*coverOffset), y-j, z+i);	
				} else {
					l = new Location(world, x+(offset*coverOffset), y-j, z-i);	
				}
				
				l.getBlock().setType(m);	
			}
		}
	}
	
	/**
	 * Saves the current screen blocks as an image template with the given name in the config
	 * 
	 * @param name The name of the image to be set in the config
	 */
	public void saveImage(String name) {

		Location l;
		
		// Create list of blocks
		List<String> blocks = new ArrayList<String>();
		
		// Populate list of blocks with ordered list of the blocks currently on the screen
		for(int i = -3; i < 9; i++) {
			for(int j = -1; j < 5; j++) {
				
				if(direction.equals("x")) {
					l = new Location(world, x+i, y-j, z+(offset*coverOffset));
				} else if(direction.equals("-x")) {
					l = new Location(world, x-i, y-j, z+(offset*coverOffset));	
				} else if(direction.equals("z")) {
					l = new Location(world, x+(offset*coverOffset), y-j, z+i);	
				} else {
					l = new Location(world, x+(offset*coverOffset), y-j, z-i);	
				}
				
				blocks.add(l.getBlock().getType().name());	
			}
		}
		
		// Save list to config under name
		ImageConfig.get().set(name, blocks);
		ImageConfig.save();
	}
	
	/**
	 * Builds an image on the game screen from the given list of blocks
	 * 
	 * @param blocks The list of blocks to be used building the screen
	 */
	public void loadImage(List<String> blocks) {
		
		// Ensure correct number of blocks
		if(!(blocks.size() == 72)) {
			Bukkit.broadcastMessage("Couldn't load image: size incorrect");
			return;
		}
		
		Location l;
		// Counter of array index (current block to place)
		int n = 0;
		
		// Place blocks on screen 
		for(int i = -3; i < 9; i++) {
			for(int j = -1; j < 5; j++) {
				
				if(direction.equals("x")) {
					l = new Location(world, x+i, y-j, z+(offset*coverOffset));
				} else if(direction.equals("-x")) {
					l = new Location(world, x-i, y-j, z+(offset*coverOffset));	
				} else if(direction.equals("z")) {
					l = new Location(world, x+(offset*coverOffset), y-j, z+i);	
				} else {
					l = new Location(world, x+(offset*coverOffset), y-j, z-i);	
				}
				
				l.getBlock().setType(Material.matchMaterial(blocks.get(n)));
				n++;
			}
		}
	}
	
	
	/**
	 * Getter method for the number of blocks to offset the cover screen from the board
	 * 
	 * @return coverOffset the number of blocks the cover screen is offset from the board
	 */
	public int getCoverOffset() {
		return this.coverOffset;
	}
	
}
