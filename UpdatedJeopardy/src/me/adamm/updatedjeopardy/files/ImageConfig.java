package me.adamm.updatedjeopardy.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Represents the object for the image config file
 * 
 * @author Adam Fairlie <adamfairlie01@gmail.com>
 */
public class ImageConfig {
	
	/* The file object and configuration object */
	private static File file;
	private static FileConfiguration configFile;
	
	/* Create or load image config file */
	public static void setup() {
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("Jeopardy").getDataFolder(), "images.yml");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException e) {
				Bukkit.getConsoleSender().sendMessage("Couldn't create image config file");
			} 
		}
		configFile = YamlConfiguration.loadConfiguration(file);
		
	}
	
	/**
	 * Getter method for configuration object
	 * 
	 * @return configFile The configuration file object
	 */
	public static FileConfiguration get() {
		return configFile;
	}
	
	/**
	 * Save contents of the file to disk
	 */
	public static void save() {
		try {
			configFile.save(file);
		} catch(IOException e) {
			Bukkit.getConsoleSender().sendMessage("Couldn't save image config file");
		}
	}
	
}
