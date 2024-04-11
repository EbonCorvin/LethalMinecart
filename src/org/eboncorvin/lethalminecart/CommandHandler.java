package org.eboncorvin.lethalminecart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class CommandHandler implements CommandExecutor {

	private LethalMinecartPlugin plugin;
	
	public CommandHandler(LethalMinecartPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equals("lm") || command.getName().equals("lethalminecart")) {
			FileConfiguration config = plugin.getConfig();
			if(config.contains(args[0])) {
				config.set(args[0], Double.parseDouble(args[1]));
				plugin.saveConfig();
				plugin.eventListeners.reloadConfig();
				sender.sendMessage("Variable " + args[0] + " set. Configuration reloaded");
				return true;
			}else
				sender.sendMessage(args[0] + " is not a valid variable!");
		}else if(command.getName().equals("lmvar") || command.getName().equals("lethalminecartvar")) {
			String[] varString = plugin.variableExplanation();
			for(String str:varString) {
				sender.sendMessage(str);
			}
		}else {
			return false;
		}
		return true;
	}
}
