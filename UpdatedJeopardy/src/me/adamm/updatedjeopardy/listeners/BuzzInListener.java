package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;
import me.adamm.updatedjeopardy.classes.JPlayer;
import me.adamm.updatedjeopardy.classes.Utils;

public class BuzzInListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	
	/*Class constructor*/
	public BuzzInListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Tag.BUTTONS.isTagged(e.getClickedBlock().getType()))) {return;}
		Game game = plugin.getGame();
		
		Location pressLocation = e.getClickedBlock().getLocation();
		
		JPlayer j = game.getPlayer(e.getPlayer().getUniqueId());
		
		if(!game.buzzersOn() || game.getPlayerBuzzed() != null || !(game.getPlayersInQuestion().contains(j))) {
			e.setCancelled(true);
			return;
		}
		
		game.setPlayerBuzzed(j);
		j.buzzIn(pressLocation.getWorld());
		Bukkit.broadcastMessage(Utils.chat("&b&l" + j.getName() + " &r&bhas buzzed in!"));
		
	}
	


	
}
