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

public class JSetCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JSetCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("jset").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Game game = plugin.getGame();
		
		
		if(args.length != 2) {
			sender.sendMessage(Utils.chat("&4Invalid command. Usage: /jset <player> <amount>"));
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
		
		try {
			int points = Integer.parseInt(args[1]);
			j.setScore(points);
			sender.sendMessage(Utils.chat("&aSet " + p.getName() + "'s points to " + points));
			return true;	
		} catch(Exception e) {
			sender.sendMessage(Utils.chat("&4Please enter a valid number of points"));
			return false;	
		}		
	}
}