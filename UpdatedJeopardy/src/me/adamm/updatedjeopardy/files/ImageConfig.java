package me.adamm.updatedjeopardy.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ImageConfig {
	
	private static File file;
	private static FileConfiguration configFile;
	
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
	
	
	public static FileConfiguration get() {
		return configFile;
	}
	
	
	public static void save() {
		try {
			configFile.save(file);
		} catch(IOException e) {
			Bukkit.getConsoleSender().sendMessage("Couldn't save image config file");
		}
	}
	
}
