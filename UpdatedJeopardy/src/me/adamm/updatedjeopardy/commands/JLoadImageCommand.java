package me.adamm.updatedjeopardy.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;
import me.adamm.updatedjeopardy.files.ImageConfig;

public class JLoadImageCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JLoadImageCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("jloadimage").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /jloadimage (template name)"));
			return false;
		}
		
		if(!sender.isOp()) {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;			
		}
		
		Game game = plugin.getGame();
		String template = args[0].toLowerCase();
		
		if(!ImageConfig.get().contains(args[0].toLowerCase())) {
			sender.sendMessage(Utils.chat("&4Could not find template " + template));
			return false;
		}
		
		List<String> blocks = ImageConfig.get().getStringList(template);
		game.loadImage(blocks);
		sender.sendMessage(Utils.chat("&aSuccessfully loaded template " + template +  "!"));
		return true;
	}
}
