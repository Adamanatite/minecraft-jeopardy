package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for the help menu */
public class JHelpCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JHelpCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jhelp").setExecutor(this);
	}
	
	/**
	 * Sends the command sender the help menu
	 *
	 * @param sender The command sender
	 */
	private void sendPlayerHelp(CommandSender sender) {
		sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
		sender.sendMessage(Utils.chat("&5&lJEOPARDY PLAYER HELP"));
		
		sender.sendMessage(Utils.chat("&d&lAnswering Questions"));
		sender.sendMessage(Utils.chat("&bAfter buzzers are activated, you can press your button to buzz in. "
				+ "The first player to buzz in must answer the question."));
		
		sender.sendMessage(Utils.chat("&d&lDaily Double"));
		sender.sendMessage(Utils.chat("&9&l/jbet <amount> &r&bto place the bet on the current daily double if possible."));
		
		sender.sendMessage(Utils.chat("&d&lFinal Jeopardy"));
		sender.sendMessage(Utils.chat("&9&l/jbet <amount> &r&bto place the bet on the final jeopardy if possible."));		
		sender.sendMessage(Utils.chat("&9&l/jfinal <answer> &r&bto answer the final jeopardy."));		
		
		sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
	}
	
	/**
	 * Sends the command sender the admin help menu
	 *
	 * @param sender The command sender
	 */
	
	private void sendAdminHelp(CommandSender sender) {
		sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
		sender.sendMessage(Utils.chat("&5&lJEOPARDY ADMIN HELP"));
		
		sender.sendMessage(Utils.chat("&d&lGame Setup"));
		sender.sendMessage(Utils.chat("&9&l/jplayer &r&bto see the current players in the game."));
		sender.sendMessage(Utils.chat("&9&l/jplayer add <player> &r&bto add the specified player to the game."));
		sender.sendMessage(Utils.chat("&9&l/jplayer remove <player> &r&bto remove the specified player from the game."));
		sender.sendMessage(Utils.chat("&9&l/jload <board> &r&bto load the given board from the config (this starts the game if it is full has not already started)."));		

		sender.sendMessage(Utils.chat("&d&lAsking Questions"));
		sender.sendMessage(Utils.chat("&9&l/jactivate &r&bto activate buzzers."));
		sender.sendMessage(Utils.chat("&9&l/janswer <answer:0|1> &r&bto evaluate a players answer (0 for incorrect, 1 for correct)."));
		sender.sendMessage(Utils.chat("&9&l/jreveal &r&bto reveal the current questions answer and end the question."));
		
		sender.sendMessage(Utils.chat("&d&lFinal Jeopardy"));
		sender.sendMessage(Utils.chat("&9&l/jfinal &r&bto begin the final jeopardy."));
		sender.sendMessage(Utils.chat("&9&l/jactivate &r&bto display the next players answer."));
		sender.sendMessage(Utils.chat("&9&l/janswer <answer:0|1> &r&bto evaluate the current answer (0 for incorrect, 1 for correct)."));
		sender.sendMessage(Utils.chat("&9&l/jreveal &r&bto reveal the answer to the final jeopardy."));		
		
		sender.sendMessage(Utils.chat("&d&lIn-game admin commands"));
		sender.sendMessage(Utils.chat("&9&l/jadd <player> <points> &r&bto add the given number of points to a players score.."));
		sender.sendMessage(Utils.chat("&9&l/jremove <player> <points> &r&bto remove the given number of points from a players score.."));
		sender.sendMessage(Utils.chat("&9&l/jset <player> <points> &r&bto set the specified players points to the given amount."));
		sender.sendMessage(Utils.chat("&9&l/jskip &r&bto skip the current question without revealing the answer."));	
		sender.sendMessage(Utils.chat("&9&l/jask <category:1-6> <question:1-5> &r&basks the given question (top to bottom) from the category (left to right)"));
		sender.sendMessage(Utils.chat("&9&l/jreset &r&bto delete the current game and create a fresh game."));		
		
		sender.sendMessage(Utils.chat("&d&lImage Question Setup"));
		sender.sendMessage(Utils.chat("&9&l/jsaveimage <name> &r&bto save the current image shown on the game board to the config under the given name."));
		sender.sendMessage(Utils.chat("&9&l/jloadimage <name> &r&bto load the image from the config onto the board (this is done automatically for image questions)."));
		
		sender.sendMessage(Utils.chat("&d&lMiscellaneous Commands"));
		sender.sendMessage(Utils.chat("&9&l/vanish &r&bto toggle visibility to other players."));
		
		sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
	}
	
	/**
	 * Sends the help menu to the command sender
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command ("admin" or "player" for the different help options)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Default help option (send non-admins standard help message, give admins choice
		if(args.length != 1 || !(args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("player"))) {
			if(!sender.isOp()) {
				this.sendPlayerHelp(sender);
				return true;
			} else {
				sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
				sender.sendMessage(Utils.chat("&5&lJEOPARDY HELP"));
				sender.sendMessage(Utils.chat("&9&l/jhelp player &r&bfor help playing."));
				sender.sendMessage(Utils.chat("&9&l/jhelp admin &r&bfor help running the game."));
				sender.sendMessage(Utils.chat("&d------------------------------------------------------"));
				return true;
			}
		}
		
		// Admin help option
		if(args[0].equalsIgnoreCase("admin")) {
			if(sender.isOp()) {
				this.sendAdminHelp(sender);
			} else {
				sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
				return false;
			}
		}
		
			
		this.sendPlayerHelp(sender);
		return true;
	}
}
