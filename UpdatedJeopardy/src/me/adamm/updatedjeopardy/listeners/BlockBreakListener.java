package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;

/* Listener class to ensure the board is not broken during a game */
public class BlockBreakListener implements Listener {
	/* Reference to main class */
	private Main plugin;
	
	/* Class constructor */
	public BlockBreakListener(Main plugin) {
		/* Register listener with plugin */
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	/**
	 * Listens for block breaks and stops the breaking of blue concrete
	 *
	 * @param e The block break event
	 */
	@EventHandler
    public void onBlueConcreteBreak(BlockBreakEvent e)
    {
		// Stop blue concrete being broken when the game is active
        if(e.getBlock().getType().equals(Material.BLUE_CONCRETE))
        {
        	Game game = plugin.getGame();
        	if(game.isOngoing()) {
        		e.setCancelled(true);
        	}
        }
    }

}
