package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;
/* Class for accepting bets on daily double and final jeopardy questions */
public class JBetCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JBetCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jbet").setExecutor(this);
	}
	
	/**
	 * Accepts a bet from a player for the double or final jeopardy
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the bet amount)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {return false;}
		Player p = (Player) sender;
		
		Game game = plugin.getGame();
		
		// Check bets are accepted
		if((game.getCurrentQuestion() == null || !(game.getCurrentQuestion().isDailyDouble())) && !game.isFinal()) {
			p.sendMessage(Utils.chat("&4It is not currently the daily double"));
			return false;
			}
		
		JPlayer j = game.getPlayer(p.getUniqueId());
		// Check player can bet
		if(j != null && (game.getPlayerBuzzed() == j || game.isFinal())) {
			// Check correct formatting and bet amount
			if(args.length != 1) {
				p.sendMessage(Utils.chat("&4Incorrect usage: /jbet <number>"));
			}
			try {
				int bet = Integer.parseInt(args[0]);
				int max_bet;
				if(game.isFinal()) {
					max_bet = Math.max(j.getScore(), 1000);
				}
				else {
					max_bet = Math.max(j.getScore(), game.getCurrentQuestion().getWorth());
				}
				
				
				if(bet < 0 || bet > max_bet){
					p.sendMessage(Utils.chat("&4Invalid bet! Please enter a number between 0 and " + max_bet));
					return false;
				}
				// Accept bet
				game.getBet(bet, j);
				p.sendMessage(Utils.chat("&aYou have successfully bet " + Utils.getScoreString(bet)));
				return true;
				
			} catch(Exception e) {
				p.sendMessage(Utils.chat("&4Incorrect usage: /jbet <number>"));
				return false;
			}
		} else {
			p.sendMessage(Utils.chat("&4You are not the player with the daily double!"));
		}
		
		return false;
		
	}
}
