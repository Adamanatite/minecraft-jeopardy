package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;

public class JRevealCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JRevealCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("jreveal").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.isOp()) {
			Game game = plugin.getGame();
			game.revealAnswer();
			return true;
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}