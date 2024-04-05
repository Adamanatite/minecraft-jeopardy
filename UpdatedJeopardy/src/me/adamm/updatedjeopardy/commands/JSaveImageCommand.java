package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
/* Command for saving an image template to the config */
public class JSaveImageCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JSaveImageCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jsaveimage").setExecutor(this);
	}
	
	/**
	 * Saves the current blocks on the board to the images config
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the name of the template)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure template name and admin use
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jsaveimage (template name)"));
			return false;
		}
		
		if(!sender.isOp()) {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;			
		}
		
		// Save image to config
		Game game = plugin.getGame();
		game.saveImage(args[0].toLowerCase());
		sender.sendMessage(Utils.chat("&aSuccessfully saved template " + args[0].toLowerCase() +  "!"));
		return true;
	}
}
