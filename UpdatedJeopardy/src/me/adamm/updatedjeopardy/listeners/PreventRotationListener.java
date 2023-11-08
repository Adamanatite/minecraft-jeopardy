package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.adamm.updatedjeopardy.Main;

public class PreventRotationListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	
	/*Class constructor*/
	public PreventRotationListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof ItemFrame && !(e.getPlayer().isOp()) && !((ItemFrame)e.getRightClicked()).getItem().getType().equals(Material.AIR)){
			e.setCancelled(true);
		}
	}
	


	
}
