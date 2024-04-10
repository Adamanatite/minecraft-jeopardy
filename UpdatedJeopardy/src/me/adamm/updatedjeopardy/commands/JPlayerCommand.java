package me.adamm.updatedjeopardy.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for managing the players in the game */
public class JPlayerCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JPlayerCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("jplayer").setExecutor(this);
	}
	
	public Player getPlayerByUUID(UUID uuid) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.getUniqueId().equals(uuid)) {
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Adds, removes, swaps or lists the players in the game
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (possibly add/remove/list/swap and 1 or 2 player names)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Game game = plugin.getGame();
		
		// List players in game
		if(args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("list"))) {
			String output = Utils.chat("&2&lCurrent players: &r&a");
			List<String> players = game.getPlayers();
			
			for(String s: players) {
				output += s + ", ";
			}
			
			sender.sendMessage(output.substring(0, output.length() - 2));
			return true;
		}
		// Swap a player in game with a player out of game
		if(args.length == 3 && args[0].equalsIgnoreCase("swap")) {
			if(!sender.hasPermission("jeopardy.editplayers")) {
				sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
				return false;
			}
			
			JPlayer j = game.getPlayerByName(args[1]);
			if(j == null) {
				sender.sendMessage(Utils.chat("&4Couldn't find player " + args[1] + " in the game"));
				sender.sendMessage(Utils.chat("&4Usage: /jplayer swap (oldPlayer) (newPlayer)"));
				return false;
			}
			
			Player p = Bukkit.getPlayer(args[2]);
			if(p == null) {
				sender.sendMessage(Utils.chat("&4Couldn't find player " + args[2]));
				sender.sendMessage(Utils.chat("&4Usage: /jplayer swap (oldPlayer) (newPlayer)"));
				return false;
			}
			
			j.setName(p.getName());
			j.setUuid(p.getUniqueId());
			sender.sendMessage(Utils.chat("&aSuccessfully replaced " + args[2]+  " with " + args[1] + "!"));
			return true;
		}
		
		// Add/remove players
		
		if(!(args.length == 2)) {
			sender.sendMessage(Utils.chat("&4Invalid command usage: /jplayer <add/remove> (player)"));
			return false;
			}
		
		// Add player to game if there is space
		
		if(args[0].equalsIgnoreCase("add")) {
			
			if(!sender.hasPermission("jeopardy.editplayers")) {
				sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
				return false;
			}
			
			Player p = Bukkit.getPlayer(args[1]);
			
			if(p == null) {
				sender.sendMessage(Utils.chat("&4Couldn't find player " + args[1]));
				return false;
			}
			
			if(game.addPlayer(p)) {
				sender.sendMessage(Utils.chat("&aAdded player " + args[1] + " to the game!"));
			} else {
				sender.sendMessage(Utils.chat("&4The game is full!"));
			}
			
			
			return true;
		}
		
		// Remove player from game if they are in game
		
		if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm")) {
			
			if(!sender.hasPermission("jeopardy.editplayers")) {
				sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
				return false;
			}
			
			Player p = Bukkit.getPlayer(args[1]);
			
			if(p == null) {
				sender.sendMessage(Utils.chat("&4Couldn't find player " + args[1]));
				return false;
			}
			
			if(game.removePlayer(p.getUniqueId())) {
				sender.sendMessage(Utils.chat("&aRemoved player " + args[1] + " from the game!"));
			} else {
				sender.sendMessage(Utils.chat("&4Player " + args[1] + " wasn't in the game!"));
			}
			
			
			return true;
		}
		
		sender.sendMessage(Utils.chat("&4Invalid command usage: /jplayer <add/remove> (player)"));
		return false;
		
	}
}
