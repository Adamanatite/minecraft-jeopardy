package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
/* Command to reveal the answer to a question */
public class JRevealCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JRevealCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jreveal").setExecutor(this);
	}
	
	/**
	 * Reveals the answer to the question (for use by game host)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure sender is admin and reveal answer
		if(sender.isOp()) {
			Game game = plugin.getGame();
			game.revealAnswer();
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
