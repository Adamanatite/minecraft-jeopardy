package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;

public class JSaveImageCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JSaveImageCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("jsaveimage").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jsaveimage (template name)"));
			return false;
		}
		
		if(!sender.isOp()) {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;			
		}
		
		Game game = plugin.getGame();
		game.saveImage(args[0].toLowerCase());
		sender.sendMessage(Utils.chat("&aSuccessfully saved template " + args[0].toLowerCase() +  "!"));
		return true;
	}
}
