package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;

public class BlockBreakListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	/*Class constructor*/
	public BlockBreakListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
    public void onItemFrameBreak(BlockBreakEvent e)
    {
		
        if(e.getBlock().getType().equals(Material.BLUE_CONCRETE))
        {
        	Game game = plugin.getGame();
        	if(game.isOngoing()) {
        		e.setCancelled(true);
        	}
        }
    }

}
