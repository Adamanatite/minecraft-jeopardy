package me.adamm.updatedjeopardy.classes;

import org.bukkit.ChatColor;

public class Utils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static String getScoreString(int score) {
		String scoreString = "";
		if(score < 0) {
			scoreString += Utils.chat("&c");
		}
		scoreString += "$";
		
		if(score < 1000 && score > -1000) {
			scoreString += String.valueOf(score);
		}
		else {
			String tempScore = String.valueOf(score);
			scoreString += tempScore.substring(0, tempScore.length() - 3) + "," + tempScore.substring(tempScore.length() - 3);
		}
		return scoreString;
	}
	
	public static String getChangeScoreString(int score) {
		String scoreString = "";
		if(score < 0) {
			scoreString += Utils.chat("&4-");
		} else {
			scoreString += Utils.chat("&a+");
		}
		scoreString += "$";
		score = Math.abs(score);
		
		
		if(score < 1000 && score > -1000) {
			scoreString += String.valueOf(score);
		}
		else {
			String tempScore = String.valueOf(score);
			scoreString += tempScore.substring(0, tempScore.length() - 3) + "," + tempScore.substring(tempScore.length() - 3);
		}
		return scoreString;
	}
	
	
}
