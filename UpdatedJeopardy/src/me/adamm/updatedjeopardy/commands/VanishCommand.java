package me.adamm.updatedjeopardy.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Utils;

/* Command for admins to vanish */
public class VanishCommand implements CommandExecutor {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public VanishCommand(Main plugin) {
		/* Register command with plugin */
		this.plugin = plugin;
		plugin.getCommand("vanish").setExecutor(this);
	}
	
	/**
	 * Toggles the visibility of the player who sends the command
	 *
	 * @param sender The command sender
	 * @param cmd The command executed
	 * @param label The alias of the command used
	 * @param args A list of the arguments passed in the command (the player to vanish, if not the sender)
	 * @return success If the command was executed successfully or was used incorrectly/had an error
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure user is an admin player
		if(sender.hasPermission("jeopardy.vanish")) {
			
			if(!(sender instanceof Player)) {
				return false;
			}
			
			Player p;
			
			// Decide if player is the sender or someone else
			if(args.length == 0) {
				p = (Player) sender;
			} else {
				try {
					p = Bukkit.getPlayer(args[0]);
				} catch(Exception e) {
					p = (Player) sender;
				}
			}
			
			// Hide or show player
			List<Player> vanishedPlayers = plugin.getVanishedPlayers();
			
			Boolean isVisible = !(vanishedPlayers.contains(p));
			
			for(Player p2 : Bukkit.getOnlinePlayers()) {
				if(isVisible) {
					p2.hidePlayer(p);
					
				} else {
					p2.showPlayer(p);
				}
			}
			if(isVisible) {
				vanishedPlayers.add(p);
				p.sendMessage(Utils.chat("&aYou have vanished"));
			} else {
				vanishedPlayers.remove(p);
				p.sendMessage(Utils.chat("&aYou have unvanished"));
			}
			
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
