package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

/* Listener class for when a player buzzes into a question */
public class BuzzInListener implements Listener {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public BuzzInListener(Main plugin) {
		/* Register listener with plugin */
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Listens for players pushing buttons and buzzes them into the question if appropriate
	 *
	 * @param e The player interaction event
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		// Check player has pushed button
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Tag.BUTTONS.isTagged(e.getClickedBlock().getType()))) {return;}
		
		// If player can't buzz in, cancel the event
		Game game = plugin.getGame();
		JPlayer j = game.getPlayer(e.getPlayer().getUniqueId());
		if(!game.buzzersOn() || game.getPlayerBuzzed() != null || !(game.getPlayersInQuestion().contains(j))) {
			if(!(e.getPlayer().isOp())) {
			e.setCancelled(true);
			}
			return;
		}
		
		// Buzz player in
		game.setPlayerBuzzed(j);
		j.buzzIn();
		Bukkit.broadcastMessage(Utils.chat("&b&l" + j.getName() + " &r&bhas buzzed in!"));
		
	}

}
