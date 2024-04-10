package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.adamm.updatedjeopardy.Main;

/* Listener class to prevent items inside the frames from being rotated */
public class PreventRotationListener implements Listener {
	/* Reference to main class */
	private Main plugin;
	
	
	/* Class constructor */
	public PreventRotationListener(Main plugin) {
		/* Register listener with plugin */
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Listens for players rotating item frames and cancels it during the game
	 *
	 * @param e The player interact with entity event
	 */
	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		// Stop non-admin players from rotating item frames during a jeopardy game
		if(e.getRightClicked() instanceof ItemFrame && !(e.getPlayer().hasPermission("jeopardy.rotateframesduringgame")) && !((ItemFrame)e.getRightClicked()).getItem().getType().equals(Material.AIR)){
			e.setCancelled(true);
		}
	}
	
}
