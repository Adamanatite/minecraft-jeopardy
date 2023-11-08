package me.adamm.updatedjeopardy.classes;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;



public class JPlayer {
	private UUID uuid;
	private String name;
	private int score;
	private Hologram h;
	private int x;
	
	
	//Hologram
	
	public JPlayer(Player p, Hologram holo, int x1) {
		this.name = p.getName();
		this.uuid = p.getUniqueId();
		this.score = 0;
		this.h = holo;
		this.x = x1;
		//Hologram
	}
	
	public void buzzIn(World world) {
		Location l = new Location(world, x, 38, -14);
		l.getBlock().setType(Material.REDSTONE_BLOCK);
		l.add(-1, 0, 0);
		l.getBlock().setType(Material.REDSTONE_BLOCK);
	}
	
	public void buzzOut(World world) {
		Location l = new Location(world, x, 38, -14);
		l.getBlock().setType(Material.AIR);
		l.add(-1, 0, 0);
		l.getBlock().setType(Material.AIR);
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void addScore(int nscore) {
		this.score += nscore;
		this.updateHologram();
	}
	
	public void removeScore(int nscore) {
		this.score -= nscore;
		this.updateHologram();
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
	
	
	
}
