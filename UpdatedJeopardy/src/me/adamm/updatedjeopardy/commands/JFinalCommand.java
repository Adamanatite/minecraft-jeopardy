package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

public class JFinalCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JFinalCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("jfinal").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Game game = plugin.getGame();
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
