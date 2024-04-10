package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for asking a given question (in case item frame is broken) */
public class JAskCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JAskCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jask").setExecutor(this);
	}
	
	/**
	 * Asks the given question (for use by game host)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (category number from 1-6 and question number from 1-5)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure correct command formatting and admin use
		if(args.length != 2 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jask <category(1-6)> <question(1-5)>"));
			return false;
		}
		
		if(sender.hasPermission("jeopardy.askquestions")) {
			try {
				int category = Integer.parseInt(args[0]);
				int question = Integer.parseInt(args[1]);
				
				
				if(category < 1 || category > 6 || question < 1 || question > 5) {
					sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jask <category(1-6)> <question(1-5)>"));
					return false;
				}
				
				// Ask given question
				Game game = plugin.getGame();
				game.askQuestion(category, question);
				return true;
				
			} catch(Exception e) {
				sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jask <category(1-6)> <question(1-5)>"));
				return false;
			}
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
