package me.adamm.updatedjeopardy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.Utils;

public class JAnswerCommand implements CommandExecutor {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public JAnswerCommand(Main plugin) {
		/*Register command with plugin*/
		this.plugin = plugin;
		plugin.getCommand("janswer").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 1 ) {
			sender.sendMessage(Utils.chat("&4Incorrect usage. Use /janswer <0/1>"));
			return false;
		}
		
		if(sender.isOp()) {
			try {
				int answer = Integer.parseInt(args[0]);
				Game game = plugin.getGame();
				if(answer == 0) {
					game.answerQuestion(false);
					return true;
				}
				else {
					game.answerQuestion(true);
					return true;
				}
				
			} catch(Exception e) {
				sender.sendMessage(Utils.chat("&4Incorrect usage. Use /janswer <0/1>"));
				return false;
			}
			
		} else {
			sender.sendMessage(Utils.chat("&4You are not authorised to perform this command."));
			return false;
		}
	}
}
