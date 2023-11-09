package me.adamm.updatedjeopardy;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.commands.JActivateCommand;
import me.adamm.updatedjeopardy.commands.JAddCommand;
import me.adamm.updatedjeopardy.commands.JAnswerCommand;
import me.adamm.updatedjeopardy.commands.JAskCommand;
import me.adamm.updatedjeopardy.commands.JBetCommand;
import me.adamm.updatedjeopardy.commands.JFinalCommand;
import me.adamm.updatedjeopardy.commands.JLoadCommand;
import me.adamm.updatedjeopardy.commands.JLoadImageCommand;
import me.adamm.updatedjeopardy.commands.JPlayerCommand;
import me.adamm.updatedjeopardy.commands.JRemoveCommand;
import me.adamm.updatedjeopardy.commands.JResetCommand;
import me.adamm.updatedjeopardy.commands.JRevealCommand;
import me.adamm.updatedjeopardy.commands.JSaveImageCommand;
import me.adamm.updatedjeopardy.commands.JSetCommand;
import me.adamm.updatedjeopardy.commands.JSkipCommand;
import me.adamm.updatedjeopardy.files.ImageConfig;
import me.adamm.updatedjeopardy.listeners.BuzzInListener;
import me.adamm.updatedjeopardy.listeners.FrameBreakListener;
import me.adamm.updatedjeopardy.listeners.PreventRotationListener;

public class Main extends JavaPlugin {
	
	private Game game;
	
	@Override
	public void onEnable() {
		
		// Set up config files
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		ImageConfig.setup();
		ImageConfig.get().options().copyDefaults();
		ImageConfig.save();
		
		// Set up game object
		this.game = new Game(this);
		game.coverScreen(Material.AIR);
		
		// Initialise commands
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
		
		// Initialise listeners
		new BuzzInListener(this);
		new FrameBreakListener(this);
		new PreventRotationListener(this);
		
	}
	
	public Game getGame() {
		return this.game;
	}
	
}
