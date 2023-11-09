package me.adamm.updatedjeopardy.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.adamm.updatedjeopardy.Main;
import me.adamm.updatedjeopardy.classes.Game;

public class FrameBreakListener implements Listener {
	/*Reference to main class*/
	private Main plugin;
	
	int x;
	int y;
	int z;
	String direction;
	World world;
	
	/*Class constructor*/
	public FrameBreakListener(Main plugin) {
		/*Register listener with plugin*/
		this.plugin = plugin;
		this.x = plugin.getConfig().getInt("board_top_left.x");
		this.y = plugin.getConfig().getInt("board_top_left.y");
		this.z = plugin.getConfig().getInt("board_top_left.z");
		this.world = Bukkit.getServer().getWorld(plugin.getConfig().getString("board_top_left.world"));
		this.direction = plugin.getConfig().getString("board_top_left.horizontal");
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(EntityDamageByEntityEvent e) {
		
		if(!(e.getEntity() instanceof ItemFrame) || !(e.getEntity().getWorld().equals(world))) {return;}
		Game game = plugin.getGame();
		
		
		if(e.getDamager() instanceof Player){
			Player p = (Player) e.getDamager();
			if(game.getPlayer(p.getUniqueId()) != null && !(p.isOp())) {
				e.setCancelled(true);
			}
		}
		
		ItemFrame frame = (ItemFrame) e.getEntity();
		Location l = frame.getLocation();
		Location board_top_left = new Location(world, x, y, z);
		
		
		int catNo;
		int diff;
		if(this.direction.equals("x") || this.direction.equals("-x")) {
			catNo = Math.abs(board_top_left.getBlockX() - l.getBlockX()) + 1;
			diff = Math.abs(board_top_left.getBlockZ() - l.getBlockZ()) + 1;
		}
		else {
			catNo = Math.abs(board_top_left.getBlockZ() - l.getBlockZ()) + 1;
			diff = Math.abs(board_top_left.getBlockX() - l.getBlockX()) + 1;
		}
		
		int quNo = Math.abs(board_top_left.getBlockY() - l.getBlockY()) + 1;
		
		if(catNo < 1 || catNo > 6 || quNo < 1 || quNo > 5 || diff < -2 || diff > 2) {return;}
		
		game.askQuestion(catNo, quNo);
		
	}
	
}
