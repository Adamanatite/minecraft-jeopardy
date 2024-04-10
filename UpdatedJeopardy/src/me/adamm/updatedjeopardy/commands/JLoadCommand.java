package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for loading a board from the config */
public class JLoadCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JLoadCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jload").setExecutor(this);
	}
	
	/**
	 * Loads a board with the given name from the config to the game board
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the board name from the config)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Ensure correct usage and admin use
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jload <board_name>"));
			return false;
		}
		
		if(sender.hasPermission("jeopardy.loadboard")) {
			// Load board from config if it exists
			String board_name = args[0].toLowerCase();
			if(!(plugin.getConfig().contains(board_name))) {
				sender.sendMessage(Utils.chat("&4Could not find the board " + board_name + " in the config."));
				return false;
			}
			plugin.getGame().loadBoard(board_name);
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
