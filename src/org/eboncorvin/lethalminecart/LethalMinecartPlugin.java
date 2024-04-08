package org.eboncorvin.lethalminecart;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LethalMinecartPlugin extends JavaPlugin {
	
	private WorldEventListener eventListeners;
	@Override
	public void onEnable() {
		FileConfiguration config = this.getConfig();
		eventListeners = new WorldEventListener(config);
		this.reloadConfig();
		eventListeners.reloadConfig();
		getServer().getPluginManager().registerEvents(eventListeners, this);
		getCommand("lm").setExecutor(new CommandHandler(this, config));
		getCommand("lethalminecart").setExecutor(new CommandHandler(this, config));
	}
	
	public void reloadEventConfig() {
		eventListeners.reloadConfig();
	}
}
