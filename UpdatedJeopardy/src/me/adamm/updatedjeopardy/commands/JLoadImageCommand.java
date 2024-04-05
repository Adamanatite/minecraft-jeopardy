package me.adamm.updatedjeopardy.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
import me.adamm.updatedjeopardy.files.ImageConfig;

/* Command for loading image boards from the config */
public class JLoadImageCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JLoadImageCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jloadimage").setExecutor(this);
	}
	
	/**
	 * Loads the given image from the config and builds it on the board
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the name of the image template in the config)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure correct usage and admin use
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jloadimage (template name)"));
			return false;
		}
		
		if(!sender.isOp()) {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;			
		}
		
		// Load template if it exists
		Game game = plugin.getGame();
		String template = args[0].toLowerCase();
		
		if(!ImageConfig.get().contains(args[0].toLowerCase())) {
			sender.sendMessage(Utils.chat("&4Could not find template " + template));
			return false;
		}
		
		List<String> blocks = ImageConfig.get().getStringList(template);
		game.loadImage(blocks);
		sender.sendMessage(Utils.chat("&aSuccessfully loaded template " + template +  "!"));
		return true;
	}
}
