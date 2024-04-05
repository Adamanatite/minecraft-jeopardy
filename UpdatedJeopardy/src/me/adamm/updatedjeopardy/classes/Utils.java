package me.adamm.updatedjeopardy.classes;

import org.bukkit.ChatColor;

/**
 * A class for utility methods useful in many areas of the program
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class Utils {

	/**
	 * Formats a string to use the special colour codes of Minecaft
	 * 
	 * @param s The human written string to be converted
	 * @return newText The converted text to be used in the game
	 */
	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	/**
	 * Formats a given integer score to a coloured and properly formatted dollar amount
	 * 
	 * @param score The score to be formatted
	 * @return scoreString The formatted score string
	 */
	public static String getScoreString(int score) {
		String scoreString = "";
		// Red for negative score
		if(score < 0) {
			scoreString += Utils.chat("&c");
		}
		scoreString += "$";
		
		// No commas case (absolute score value less than 1000)
		if(score < 1000 && score > -1000) {
			scoreString += String.valueOf(score);
		}
		else {
			// Add comma after the third digit
			String tempScore = String.valueOf(score);
			scoreString += tempScore.substring(0, tempScore.length() - 3) + "," + tempScore.substring(tempScore.length() - 3);
		}
		return scoreString;
	}
	
	/**
	 * Formats a given integer score change to a coloured and properly formatted dollar change amount
	 * 
	 * @param score The score change to be formatted
	 * @return scoreString The formatted score string
	 */
	public static String getChangeScoreString(int score) {
		String scoreString = "";
		// Decide colour and +/-
		if(score < 0) {
			scoreString += Utils.chat("&4-");
		} else {
			scoreString += Utils.chat("&a+");
		}
		scoreString += "$";
		score = Math.abs(score);
		
		// No commas case (absolute score value less than 1000)
		if(score < 1000 && score > -1000) {
			scoreString += String.valueOf(score);
		}
		else {
			// Add comma after the third digit
			String tempScore = String.valueOf(score);
			scoreString += tempScore.substring(0, tempScore.length() - 3) + "," + tempScore.substring(tempScore.length() - 3);
		}
		return scoreString;
	}
	
	
}
