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

public class Game {
	private int noAnswered;
	private JPlayer[] players;
	private Set<JPlayer> playersInQuestion;
	private JPlayer playerBuzzed;
	private Question currentQuestion;
	Question[][] questions;
	
	private boolean onGoing;
	private boolean buzzersOn;
	private boolean isFinal;
	private JPlayer playerInControl;
	
	int maxCharLength;
	
	private String direction;
	private int offset;
	private World world;
	private int x;
	private int y;
	private int z;
	
	private HashMap<JPlayer, Integer> finalBets;
	private HashMap<JPlayer, String> finalAnswers;
	
	
	Hologram qHolo;
	Hologram[] pHolos;
	Location[] buzzers;
	
	private Main plugin;
	
	
	public Game(Main plugin) {
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
		this.isFinal = false;
		
		
		this.finalBets = new HashMap<JPlayer, Integer>();
		this.finalAnswers = new HashMap<JPlayer, String>();
		
		this.direction = plugin.getConfig().getString("board_top_left.horizontal");
		this.offset = plugin.getConfig().getInt("board_top_left.out_offset");
		this.world = Bukkit.getServer().getWorld(plugin.getConfig().getString("board_top_left.world"));
		this.x = plugin.getConfig().getInt("board_top_left.x");
		this.y = plugin.getConfig().getInt("board_top_left.y");
		this.z = plugin.getConfig().getInt("board_top_left.z");
		
		this.qHolo = this.loadHologram("question");
		this.pHolos = new Hologram[3];
		this.buzzers = new Location[3];
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

	
	private Hologram loadHologram(String name) {
		double x = plugin.getConfig().getDouble("hologram." + name + ".x");
		double y = plugin.getConfig().getDouble("hologram." + name + ".y");
		double z = plugin.getConfig().getDouble("hologram." + name + ".z");
		Location l = new Location(world, x, y, z);
		
		Hologram h = DHAPI.createHologram(name, l);
		return h;
		
	}
	
	public boolean isInGame(JPlayer j) {
		return (players[0].equals(j) || players[0].equals(j) || players[0].equals(j));
	}
	
	public JPlayer getPlayer(UUID uuid) {
		
		for(JPlayer j : this.players) {
			if(j != null && j.getUuid().equals(uuid)) {
				return j;
			}
		}
		
		return null;
	}
	
	public JPlayer getPlayerBuzzed() {
		return playerBuzzed;
	}

	public void setPlayerBuzzed(JPlayer playerBuzzed) {
		this.playerBuzzed = playerBuzzed;
		this.buzzersOn = false;
	}

	public Set<JPlayer> getPlayersInQuestion(){
		return this.playersInQuestion;
	}
	
	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(Question currentQuestion) {
		this.currentQuestion = currentQuestion;
	}
	
	public boolean addPlayer(Player p) {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i] == null) {
				JPlayer j = new JPlayer(p, pHolos[i], buzzers[i]); 
				this.players[i] = j;
				return true;
			}
		}
		return false;
	}
	
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
	
	public void resetScores() {
		for(JPlayer j : this.players) {
			if(j != null) {
				j.resetScore();
			}
		}
	}
	
public void loadBoard(String board_name) {
		
		if(!(plugin.getConfig().contains(board_name))) return;

		final int worth_mul = plugin.getConfig().getInt(board_name + ".cat_1.1.Worth");
		
		//Load category data
		for(int c = 1; c < 7; c++) {
			
			//Get category name
			String cname = plugin.getConfig().getString(board_name + ".cat_" + c + ".Name");
			
			final int c_final = c;
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    @Override
			    public void run() {
			        Location l;
					//Get sign location
					if(direction.equals("x")) {
						l = new Location(world, x+c_final-1, y+1, z+(offset*2));
					} else if(direction.equals("-x")) {
						l = new Location(world, x-c_final+1, y+1, z+(offset*2));	
					} else if(direction.equals("z")) {
						l = new Location(world, x+(offset*2), y+1, z+c_final-1);	
					} else {
						l = new Location(world, x+(offset*2), y+1, z-c_final+1);	
					}
			    	
					//Create sign
					l.getBlock().setType(Material.CRIMSON_WALL_SIGN);
					Sign sign = (Sign) l.getBlock().getState();
					WallSign bd = (WallSign) sign.getBlockData();
					bd.setFacing(BlockFace.NORTH);
					sign.setBlockData(bd);
					
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
					
					for(int i = 0; i < 5; i++) {
						
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
					
					
					l.getWorld().playSound(l, Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
					
			        for(Player p : Bukkit.getOnlinePlayers()) {
			        	if(p.getWorld().equals(world)) {
			        	p.sendTitle(cname, "", 1, 20, 1);
			        	}
			        } 
			    }
			}, 40L * c);
			
			//Populate all category questions
			for(int q = 1; q < 6; q++) {

				String qname = plugin.getConfig().getString(board_name + ".cat_" + c + "." + q + ".Question");
				String aname = plugin.getConfig().getString(board_name + ".cat_" + c + "." + q + ".Answer");
				int worth = plugin.getConfig().getInt(board_name + ".cat_" + c + "." + q + ".Worth");
				boolean daily_double = plugin.getConfig().contains(board_name + ".cat_" + c + "." + q + ".daily_double");
				Question qu = new Question(qname, aname, cname, worth, daily_double);
				this.questions[c-1][q-1] = qu;	
				
			}
		}
		
		this.noAnswered = 0;
		this.startGame();
		
	}
	
	public boolean startGame() {
		for(JPlayer j : this.players) {
			if(j == null) {
				return false;
			}
		}
		
		if (this.questions[0][0] == null){
			return false;
		}
		
		this.onGoing = true;
		this.playerInControl = this.players[0];
		this.buzzersOn = false;
		return true;
	}
	
	public void stopGame() {
		this.onGoing = false;
	}

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
	
	//Get a given question at an index
	private Question get_question(int cindex, int qindex) {
		return this.questions[cindex-1][qindex-1];
	}
	
	public void askQuestion(int cindex, int qindex) {
		if(!(this.onGoing)) {return;}
		
		Question q = get_question(cindex, qindex);
		this.currentQuestion = q;
		
		if(q.isDailyDouble()) {
			this.buzzersOn = false;
			this.playerBuzzed = this.playerInControl;
			this.playerBuzzed.buzzIn();
			this.playersInQuestion.add(this.playerInControl);
			//Daily double shit (ask question and set new worth)
			DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("DAILY DOUBLE:")));
			int s = this.playerInControl.getScore();
			String scoreString;
			if(s < 0) {
				scoreString = Utils.getScoreString(q.getWorth());
			}
			else {
				scoreString = Utils.getScoreString(Math.max(q.getWorth(), s));
			}
			coverScreen(Material.BLUE_CONCRETE, 3);
			DHAPI.addHologramLine(qHolo, Utils.chat("Enter an amount from $0 to " + scoreString));
			DHAPI.addHologramLine(qHolo, Utils.chat("A correct answer will earn you this amount"));
			DHAPI.addHologramLine(qHolo, Utils.chat("An incorrect answer will lose you this amount"));
			DHAPI.addHologramLine(qHolo, Utils.chat("Place a bet using &l/jbet <amount>"));
			
		}
		else {
			for(JPlayer j : this.players) {
				this.playersInQuestion.add(j);
			}
			
			askCurrentQuestion();
			
			
		}
		
	}
	
	public void askFinalBets() {
		this.isFinal = true;
		
		coverScreen(Material.BLUE_CONCRETE, 3);
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));
		DHAPI.addHologramLine(qHolo, Utils.chat("Bet up to how much you have (or $1000 if you have less)"));
		DHAPI.addHologramLine(qHolo, Utils.chat("An incorrect answer will lose you this amount"));
		DHAPI.addHologramLine(qHolo, Utils.chat("Place a bet using &l/jbet <amount>"));
	}
	
	
	public void getBet(int bet, JPlayer j) {
		
		if(this.currentQuestion != null && this.currentQuestion.isDailyDouble()) {
			this.currentQuestion.setWorth(bet);
			askCurrentQuestion();
			return;
		}
		
		if(!this.finalBets.containsKey(j)) {
			this.finalBets.put(j, bet);
		}
		
		if(this.finalBets.size() == this.players.length) {
			//Ask question
			askFinalQuestion();
		}
		
	}
	
	public void askFinalQuestion() {
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));

		String qString = plugin.getConfig().getString("final.Question");
		for(int i = 0; i < 4; i++) {
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
	
	public void getFinalAnswer(String ans, JPlayer j) {
		//Final jeopardy
		
		
		if(!this.finalAnswers.containsKey(j)) {
			this.finalAnswers.put(j, ans);
		}
		
		if(this.finalAnswers.size() == this.players.length) {		
			Bukkit.broadcastMessage(Utils.chat("&aAll players have locked in their answer!"));
			this.world.playSound(qHolo.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
			
			//Re-add all lines except last and replace last line
			DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7FINAL JEOPARDY")));
			String qString = plugin.getConfig().getString("final.Question");
			for(int i = 0; i < 4; i++) {
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
	
	public void showFinalAnswer() {
			
			int score = Integer.MAX_VALUE;
			for(JPlayer j: this.finalBets.keySet()) {
				if(j.getScore() < score) {
					this.playerBuzzed = j;
					score = j.getScore();
				}
			}
			this.playerBuzzed.buzzIn();
			Bukkit.broadcastMessage(Utils.chat("&b" + this.playerBuzzed.getName() + " has answered: &l" + this.finalAnswers.get(this.playerBuzzed)));
			
	}
	
	public JPlayer getPlayerByIndex(int i) {
		if(i < 0 || i > this.players.length) {
			return null;
		}
		return this.players[i];
	}
	
	public void answerFinalQuestion(boolean wasRight) {
		
		if(!(this.onGoing) || this.playerBuzzed == null) {return;}
		
		int bet = this.finalBets.getOrDefault(this.playerBuzzed, 0);
		
		if(wasRight) {
			this.playerBuzzed.addScore(bet);
			Bukkit.broadcastMessage(Utils.chat("&a" + this.playerBuzzed.getName() + " just won " + Utils.getScoreString(bet) + "!"));
		} else {
			this.playerBuzzed.removeScore(bet);
			Bukkit.broadcastMessage(Utils.chat("&c" + this.playerBuzzed.getName() + " just lost " + Utils.getScoreString(bet)));
		}
		this.playerBuzzed.buzzOut();
		this.finalAnswers.remove(this.playerBuzzed);
		this.finalBets.remove(this.playerBuzzed);
		
		if(this.finalBets.isEmpty()) {
			Bukkit.broadcastMessage(Utils.chat("&bJeopardy is over!"));
			this.revealAnswer();
			this.isFinal = false;
			return;
		}
		
	}
	
	
	public boolean isFinal() {
		return this.isFinal;
	}
	
	public void askCurrentQuestion() {
		this.buzzersOn = false;
		Question q = this.currentQuestion;
		
		DHAPI.setHologramLines(qHolo, Arrays.asList(Utils.chat("&7" + q.getCategory() + " for " + Utils.getScoreString(q.getWorth()))));
		
		String qString = q.getQuestion();
		for(int i = 0; i < 4; i++) {
			if(qString.length() < this.maxCharLength) {
				DHAPI.addHologramLine(qHolo, qString);
				break;
			}
			int index = qString.substring(0, this.maxCharLength).lastIndexOf(' ');
			DHAPI.addHologramLine(qHolo, qString.substring(0, index));
			qString = qString.substring(index+1);
		}
		coverScreen(Material.BLUE_CONCRETE, 3);
	}
	
	
	public boolean buzzersOn() {
		return this.buzzersOn;
	}
	
	public void activateBuzzers() {
		if(this.isFinal && !this.finalAnswers.isEmpty()) {
				this.showFinalAnswer();
				return;
		}
		this.buzzersOn = true;
	}
	
	public void answerQuestion(boolean wasRight) {
		
		if(this.isFinal) {
			answerFinalQuestion(wasRight);
			return;
		}
		
		if(!(this.onGoing) || this.playerBuzzed == null || this.currentQuestion == null) {return;}
		
		if(wasRight) {
			this.playerBuzzed.addScore(this.currentQuestion.getWorth());
			this.playerInControl = this.playerBuzzed;
			this.playerBuzzed.buzzOut();
			this.playerBuzzed = null;
			this.revealAnswer();
			//Empty set
			
		} else {
			this.playerBuzzed.removeScore(this.currentQuestion.getWorth());
			this.playersInQuestion.remove(this.playerBuzzed);
			this.playerBuzzed.buzzOut();
			this.playerBuzzed = null;
			if(this.playersInQuestion.isEmpty()) {
				this.revealAnswer();
			}
		}
		
	}
	
	public void revealAnswer() {
		
		if(this.isFinal()) {
			DHAPI.addHologramLine(qHolo, Utils.chat("Answer: " + plugin.getConfig().getString("final.Answer")));
		}
		else {
		if(this.currentQuestion == null) {return;}
		
		if(this.getPlayerBuzzed() != null) {
			this.answerQuestion(false);
		}
		
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
		//Hologram
		DHAPI.addHologramLine(qHolo, Utils.chat("Answer: " + this.currentQuestion.getAnswer()));
		
		this.currentQuestion = null;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	DHAPI.setHologramLines(qHolo, Arrays.asList(""));
		        coverScreen(Material.AIR, 3);
		    }
		}, 80L);
		
	}
	
	
	public void coverScreen(Material m, int offsetM) {
		Location l;
		for(int i = -3; i < 9; i++) {
			for(int j = 0; j < 5; j++) {
				
				if(direction.equals("x")) {
					l = new Location(world, x+i, y-j, z+(offset*offsetM));
				} else if(direction.equals("-x")) {
					l = new Location(world, x-i, y-j, z+(offset*offsetM));	
				} else if(direction.equals("z")) {
					l = new Location(world, x+(offset*offsetM), y-j, z+i);	
				} else {
					l = new Location(world, x+(offset*offsetM), y-j, z-i);	
				}
				
				l.getBlock().setType(m);
				
			}
		}

	}
	
	
}
