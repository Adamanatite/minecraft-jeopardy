package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for skipping the current jeopardy question */
public class JSkipCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JSkipCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jskip").setExecutor(this);
	}
	
	/**
	 * Skips the current question on the screen (for use by game admins)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure admin use and skip question
		if(sender.isOp()) {
			Game game = plugin.getGame();
			game.skipQuestion();
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
