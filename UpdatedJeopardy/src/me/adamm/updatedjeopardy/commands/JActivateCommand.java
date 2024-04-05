package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
/* Command for activating player buzzers */
public class JActivateCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JActivateCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jactivate").setExecutor(this);
	}
	
	/**
	 * Activates the buzzers after a question is asked (for use by the game host)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Ensure the user is an admin and activate buzzers
		if(sender.isOp()) {
			Game game = plugin.getGame();
			game.activateBuzzers();
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
