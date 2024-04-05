package me.adamm.updatedjeopardy.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.adamm.updatedjeopardy.Main;
/** Listener class to ensure when a player joins the vanished players are hidden for them */
public class JoinVanishListener implements Listener {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public JoinVanishListener(Main plugin) {
		/* Register listener with plugin */
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Listens for players joining and hides the vanished players from them
	 *
	 * @param e The player join event
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		List<Player> vanishedPlayers = plugin.getVanishedPlayers();
		
		for(Player p : vanishedPlayers) {
			e.getPlayer().hidePlayer(p);
		}
		
	}
	
}
