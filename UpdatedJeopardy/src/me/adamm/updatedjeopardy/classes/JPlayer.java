package me.adamm.updatedjeopardy.classes;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import me.adamm.updatedjeopardy.Main;


/**
 * Represents the state of a Jeopardy player in the game
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class JPlayer {
	/* Player username and unique ID*/
	private UUID uuid;
	private String name;
	/* Current game score*/
	private int score;
	/* Hologram to show player score (and answer to final jeopardy) */
	private Hologram h;
	/* Location of buzzer */
	private Location buzzer;
	/* Reference to main class */
	private Main plugin;
	
	/**
	 * Creates a new Player object
	 * 
	 * @param p The player in game to base the object on
	 * @param holo The hologram above the player's table
	 * @param buzzer The location of the player's buzzer
	 * @param plugin A reference to the main class
	 */
	public JPlayer(Player p, Hologram holo, Location buzzer, Main plugin) {
		// Assign variables
		this.name = p.getName();
		this.uuid = p.getUniqueId();
		this.score = 0;
		this.h = holo;
		this.buzzer = buzzer;
		this.plugin = plugin;
	}
	
	/**
	 * Buzz the player into the current question
	 */
	public void buzzIn() {
		
		// Play sound and turn on lights on player's table (by creating a redstone block below)
		this.buzzer.getWorld().playSound(this.buzzer, Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.0F, 1.0F);
		this.buzzer.add(0, -2, 0);
		Block ub = this.buzzer.getBlock();
		ub.setType(Material.REDSTONE_BLOCK);
		// Replace neighbouring air blocks with redstone blocks
		for (BlockFace face : BlockFace.values()){
			Block b = this.buzzer.getBlock().getRelative(face);
			if(b.getType().equals(Material.AIR)) {
				b.setType(Material.REDSTONE_BLOCK);
			};
		}
		this.buzzer.add(0, 2, 0);
	}
	
	/**
	 * Buzz the player out of the current question
	 */
	public void buzzOut() {
		// Replace any redstone under buzzer to air (to turn off lights on table)
		this.buzzer.add(0, -2, 0);
		Block ub = this.buzzer.getBlock();
		ub.setType(Material.AIR);
		// Replace neighbouring redstone blocks with air blocks
		for (BlockFace face : BlockFace.values()){
			Block b = this.buzzer.getBlock().getRelative(face);
			if(b.getType().equals(Material.REDSTONE_BLOCK)) {
				b.setType(Material.AIR);
			};
		}
		this.buzzer.add(0, 2, 0);
	}
	
	/**
	 * Getter method for the player's current score
	 * 
	 * @return score The player's current score
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * Adds the given amount to the player's score
	 * 
	 * @param nscore The score to add
	 */
	public void addScore(int nscore) {
		this.score += nscore;
		// Animate new score to show score change
		this.setHologram(Utils.getChangeScoreString(nscore));
		// Reset hologram after a second
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	updateHologram();
		    }
		}, 10L);
	}
	
	/**
	 * Removes the given amount from the player's score
	 * 
	 * @param nscore The score to remove
	 */
	public void removeScore(int nscore) {
		// Reuse add score method but with a negative amount
		this.addScore(-nscore);
	}
	
	/**
	 * Sets the player's score to the new score
	 * 
	 * @param nscore The new score of the player
	 */
	public void setScore(int nscore) {
		this.score = nscore;
		this.updateHologram();
	}
	
	/**
	 * Resets the player's score
	 */
	public void resetScore() {
		this.setScore(0);
		this.updateHologram();
	}
	
	/**
	 * Updates the player's hologram with their new score
	 */
	public void updateHologram() {
		// Set hologram to this.score formatted as a money amount
		String scoreString = Utils.getScoreString(this.score);
		DHAPI.setHologramLine(h, 0, scoreString);
	}
	
	/**
	 * Sets the player's hologram to the given text
	 * 
	 * @param text The new text for the hologram
	 */
	public void setHologram(String text) {
		DHAPI.setHologramLine(h, 0, text);
	}
	
	/**
	 * Getter method for the player's UUID
	 * 
	 * @return uuid The UUID of the player
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * Setter method for the player's UUID
	 * 
	 * @param uuid The new UUID of the player
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Getter method for the player's username
	 * 
	 * @return name The name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the player's username
	 * 
	 * @param name The new username of the player
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for the player's buzzer location
	 * 
	 * @return buzzer The location of the player's buzzer
	 */
	public Location getBuzzerLocation() {
		return this.buzzer;
	}
	
}
