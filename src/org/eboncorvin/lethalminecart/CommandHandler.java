package org.eboncorvin.lethalminecart;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandHandler implements CommandExecutor {

	private FileConfiguration config;
	private LethalMinecartPlugin plugin;
	
	public CommandHandler(LethalMinecartPlugin plugin, FileConfiguration config) {
		this.config = config;
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("lm") || command.getName().equals("lethalminecart")) {
			if(config.contains(args[0])) {
				config.set(args[0], Double.parseDouble(args[1]));
				plugin.saveConfig();
				plugin.reloadEventConfig();
				return true;
			}
		}
		return false;
	}

}
