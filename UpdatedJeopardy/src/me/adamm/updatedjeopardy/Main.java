package me.adamm.updatedjeopardy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.commands.JActivateCommand;
import me.adamm.updatedjeopardy.commands.JAddCommand;
import me.adamm.updatedjeopardy.commands.JAnswerCommand;
import me.adamm.updatedjeopardy.commands.JAskCommand;
import me.adamm.updatedjeopardy.commands.JBetCommand;
import me.adamm.updatedjeopardy.commands.JFinalCommand;
import me.adamm.updatedjeopardy.commands.JHelpCommand;
import me.adamm.updatedjeopardy.commands.JLoadCommand;
import me.adamm.updatedjeopardy.commands.JLoadImageCommand;
import me.adamm.updatedjeopardy.commands.JPlayerCommand;
import me.adamm.updatedjeopardy.commands.JRemoveCommand;
import me.adamm.updatedjeopardy.commands.JResetCommand;
import me.adamm.updatedjeopardy.commands.JRevealCommand;
import me.adamm.updatedjeopardy.commands.JSaveImageCommand;
import me.adamm.updatedjeopardy.commands.JSetCommand;
import me.adamm.updatedjeopardy.commands.JSkipCommand;
import me.adamm.updatedjeopardy.commands.VanishCommand;
import me.adamm.updatedjeopardy.files.ImageConfig;
import me.adamm.updatedjeopardy.listeners.BlockBreakListener;
import me.adamm.updatedjeopardy.listeners.BuzzInListener;
import me.adamm.updatedjeopardy.listeners.FrameBreakListener;
import me.adamm.updatedjeopardy.listeners.ItemFrameBreakListener;
import me.adamm.updatedjeopardy.listeners.JoinVanishListener;
import me.adamm.updatedjeopardy.listeners.LeaveVanishListener;
import me.adamm.updatedjeopardy.listeners.PreventRotationListener;

/**
 * Represents the main class, which is called when the plugin is loaded
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class Main extends JavaPlugin {
	
	// The game object used to store information about the game state
	private Game game;
	// A list of all the players that are currently invisible (for dealing with joining/leaving players)
	private List<Player> vanishedPlayers;
	
	/**
	 * Called when the plugin is enabled, dictates load behaviour
	 * 
	 */
	@Override
	public void onEnable() {
		
		// Set up config files	
		ImageConfig.setup();
		ImageConfig.get().options().copyDefaults();
		ImageConfig.save();
		
		this.vanishedPlayers = new ArrayList<Player>();
		
		// Set up game object
		this.game = new Game(this);
		
		// Ensure game board is not covered up
		game.coverScreen(Material.AIR);
		
		// Initialise plugin commands
		new JLoadCommand(this);
		new JPlayerCommand(this);
		new JActivateCommand(this);
		new JAskCommand(this);
		new JAnswerCommand(this);
		new JRevealCommand(this);
		new JBetCommand(this);
		new JResetCommand(this);
		new JFinalCommand(this);
		new JAddCommand(this);
		new JSetCommand(this);
		new JRemoveCommand(this);
		new JSkipCommand(this);
		new JSaveImageCommand(this);
		new JLoadImageCommand(this);
		new JHelpCommand(this);
		new VanishCommand(this);
		
		// Initialise listeners
		new BuzzInListener(this);
		new FrameBreakListener(this);
		new PreventRotationListener(this);
		new LeaveVanishListener(this);
		new JoinVanishListener(this);
		new ItemFrameBreakListener(this);
		new BlockBreakListener(this);
		
		
		// Show all players
		for(Player p : Bukkit.getOnlinePlayers()) {
			for(Player q : Bukkit.getOnlinePlayers()) {
				p.showPlayer(q);
			}
		}
		
	}
	
	/**
	* Returns the game object
	*
	* @return      the game object
	* @see         Game
	*/
	public Game getGame() {
		return this.game;
	}
	
	
	/**
	* Returns the list of vanished players
	*
	* @return      The list of vanished players
	*/
	public List<Player> getVanishedPlayers(){
		return this.vanishedPlayers;
	}
	
}
