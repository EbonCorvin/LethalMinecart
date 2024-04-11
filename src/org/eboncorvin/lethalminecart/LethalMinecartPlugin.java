package org.eboncorvin.lethalminecart;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LethalMinecartPlugin extends JavaPlugin {
	protected final static String[] ARR_VARIABLES = new String[] {
		"max_damage", "fatal_speed", "harmful_speed"	     
	};
	
	protected final static String[] ARR_VARIABLE_TOSTRING = new String[] {
		"Max Damage At Fatal Speed: ", "Fatal Speed:", "Harmful Speed:"	     
	};
	
	protected WorldEventListener eventListeners;
	
	@Override
	public void onEnable() {
		eventListeners = new WorldEventListener(this);
		eventListeners.reloadConfig();
		CommandHandler cmd = new CommandHandler(this);
		getServer().getPluginManager().registerEvents(eventListeners, this);
		getCommand("lm").setExecutor(cmd);
		getCommand("lethalminecart").setExecutor(cmd);
		getCommand("lmvar").setExecutor(cmd);
		getCommand("lethalminecartvar").setExecutor(cmd);

	}
	
	protected String[] variableExplanation() {
		FileConfiguration pluginConfig = this.getConfig();
		String[] returnVal = new String[ARR_VARIABLES.length];
		int idx = 0;
		for(String key:ARR_VARIABLES) {
			double value = pluginConfig.getDouble(key);
			returnVal[idx] = ARR_VARIABLE_TOSTRING[idx++] + Double.toString(value);
		}
		return returnVal;
	}
}
