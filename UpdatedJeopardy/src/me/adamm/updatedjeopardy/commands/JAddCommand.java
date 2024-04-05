package me.adamm.updatedjeopardy.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for adding points to players scores */
public class JAddCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JAddCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jadd").setExecutor(this);
	}
	
	/**
	 * Adds points to the given players score (for use by game admins)
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the player name and number of points to add)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Game game = plugin.getGame();
		
		// Ensure correct command format (player in games name then valid score number)
		if(args.length != 2) {
			sender.sendMessage(Utils.chat("&4Invalid command. Usage: /jadd <player> <amount>"));
			return false;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		
		if(p == null) {
			sender.sendMessage(Utils.chat("&4Could not find player " + args[0]));
			return false;			
		}
		
		JPlayer j = game.getPlayer(p.getUniqueId());
		if(j == null) {
			sender.sendMessage(Utils.chat("&4This player is not in the game."));
			return false;	
		}
		
		// Add the points to the players score total
		try {
			int points = Integer.parseInt(args[1]);
			j.addScore(points);
			sender.sendMessage(Utils.chat("&aGiven " + points + " points to " + p.getName()));
			return true;	
		} catch(Exception e) {
			sender.sendMessage(Utils.chat("&4Please enter a valid number of points"));
			return false;	
		}		
	}
}
