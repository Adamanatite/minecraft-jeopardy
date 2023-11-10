package me.adamm.updatedjeopardy.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.adamm.updatedjeopardy.Main;

public class LeaveVanishListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public LeaveVanishListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerQuitEvent e) {
		
		List<Player> vanishedPlayers = plugin.getVanishedPlayers();
		
		if (vanishedPlayers.contains(e.getPlayer())) {
			vanishedPlayers.remove(e.getPlayer());
		}
		
	}
	
}
