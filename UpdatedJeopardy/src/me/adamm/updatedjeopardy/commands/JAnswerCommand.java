package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
/* Command for answering a question correct or incorrect for a player (for use by game host) */
public class JAnswerCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JAnswerCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("janswer").setExecutor(this);
	}
	
	/**
	 * Answers the question correctly or incorrectly for the currently buzzed in player (for use by the game host)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (a single argument, 1/0 for correct/incorrect answer)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Check correct command usage and admin
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /janswer <0/1>"));
			return false;
		}
		
		if(sender.hasPermission("jeopardy.answerquestions")) {
			// Give correct or incorrect answer
			try {
				int answer = Integer.parseInt(args[0]);
				Game game = plugin.getGame();
				if(answer == 0) {
					game.answerQuestion(false);
					return true;
				}
				else {
					game.answerQuestion(true);
					return true;
				}
				
			} catch(Exception e) {
				sender.sendMessage(Utils.chat("&4Incorrect usage. Use /janswer <0/1>"));
				return false;
			}
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
