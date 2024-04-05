package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for sending answers to the final jeopardy question */
public class JFinalCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JFinalCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jfinal").setExecutor(this);
	}
	
	/**
	 * Accepts a players answer to the final jeopardy question or begins the final jeopardy
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (all the words used 
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Game game = plugin.getGame();
		// Begin final jeopardy if used by an admin
		if(args.length == 0 && sender.isOp()) {
			if(!game.isFinal()) {
				game.askFinalBets();
				return true;
			} else {
				sender.sendMessage(Utils.chat("&4Please answer the question"));
				return false;
			}
		}
		
		if(args.length == 0) {
			sender.sendMessage(Utils.chat("&4Please answer the question"));
			return false;
		}
		
		// Accept player answer (combine all arguments split by space)
		if(!game.isFinal()) {return false;}
		
		if(!(sender instanceof Player)) {return false;}
		
		Player p = (Player) sender;
		
		JPlayer j = game.getPlayer(p.getUniqueId());
		if(j == null) {return false;}
		
		String answerString = "";
		
		for(int i = 0; i < args.length; i++) {
			answerString += args[i] + " ";
		}
		
		answerString = answerString.substring(0, answerString.length() - 1);
		game.getFinalAnswer(answerString, j);
		p.sendMessage(Utils.chat("&aSuccessfully answered final jeopardy!"));
		
		return true;
	}
}
