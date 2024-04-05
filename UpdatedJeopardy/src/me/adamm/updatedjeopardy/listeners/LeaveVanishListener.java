package me.adamm.updatedjeopardy.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.adamm.updatedjeopardy.Main;
/* Listener class to remove players from the vanished players list if they leave the game */
public class LeaveVanishListener implements Listener {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public LeaveVanishListener(Main plugin) {
		/* Register listener with plugin */
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Listens for players leaving and removes them from the vanished players list
	 *
	 * @param e The player leave event
	 */
	@EventHandler
	public void onJoin(PlayerQuitEvent e) {
		
		List<Player> vanishedPlayers = plugin.getVanishedPlayers();
		
		if (vanishedPlayers.contains(e.getPlayer())) {
			vanishedPlayers.remove(e.getPlayer());
		}
		
	}
	
}
