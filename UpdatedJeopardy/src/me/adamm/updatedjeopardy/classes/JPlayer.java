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



public class JPlayer {
	private UUID uuid;
	private String name;
	private int score;
	private Hologram h;
	private Location buzzer;
	private Main plugin;
	
	//Hologram
	
	public JPlayer(Player p, Hologram holo, Location buzzer, Main plugin) {
		this.name = p.getName();
		this.uuid = p.getUniqueId();
		this.score = 0;
		this.h = holo;
		this.buzzer = buzzer;
		this.plugin = plugin;
	}
	
	public void buzzIn() {
		
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
	
	public void buzzOut() {
		
		this.buzzer.add(0, -2, 0);
		Block ub = this.buzzer.getBlock();
		ub.setType(Material.AIR);
		// Replace neighbouring air blocks with redstone blocks
		for (BlockFace face : BlockFace.values()){
			Block b = this.buzzer.getBlock().getRelative(face);
			if(b.getType().equals(Material.REDSTONE_BLOCK)) {
				b.setType(Material.AIR);
			};
		}
		this.buzzer.add(0, 2, 0);
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void addScore(int nscore) {
		this.score += nscore;
		this.setHologram(Utils.getChangeScoreString(nscore));
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    @Override
		    public void run() {
		    	updateHologram();
		    }
		}, 10L);
	}
	
	public void removeScore(int nscore) {
		this.addScore(-nscore);
	}
	
	public void setScore(int nscore) {
		this.score = nscore;
		this.updateHologram();
	}
	
	public void resetScore() {
		this.setScore(0);
		this.updateHologram();
	}
	
	public void updateHologram() {
		String scoreString = Utils.getScoreString(this.score);
		//Set hologram to this.score
		DHAPI.setHologramLine(h, 0, scoreString);
	}

	public void setHologram(String text) {
		//Set hologram to text
		DHAPI.setHologramLine(h, 0, text);
	}
	
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Location getBuzzerLocation() {
		return this.buzzer;
	}
	
}
