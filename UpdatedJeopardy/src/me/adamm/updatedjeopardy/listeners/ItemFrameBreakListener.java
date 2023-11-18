package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;

public class ItemFrameBreakListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public ItemFrameBreakListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
    public void onItemFrameBreak(HangingBreakByEntityEvent e)
    {
		
        if(e.getEntity() instanceof ItemFrame)
        {
        	Game game = plugin.getGame();
        	if(game.isOngoing()) {
        		e.setCancelled(true);
        	}
        }
    }

}
