package com.raws.conductor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ConductorPlugin extends JavaPlugin {
	
	protected static final String MINECART_MANIA_PLUGIN_NAME = "Minecart Mania Core";
	
	private ConductorVehicleListener vehicleListener;
	
	@Override
	public void onEnable() {
		if (!getServer().getPluginManager().isPluginEnabled(MINECART_MANIA_PLUGIN_NAME)) {
			log(Level.SEVERE, "Minecart Mania Core is required. Conductor is disabled.");
			setEnabled(false);
		}
		
		if (isEnabled()) {
			vehicleListener = new ConductorVehicleListener(this);
			// TODO Register event handlers
			log("Version " + getDescription().getVersion() + " loaded.");
		}
	}

	@Override
	public void onDisable() {
		// Do nothing
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return super.onCommand(sender, cmd, commandLabel, args);
	}

	protected Logger getLogger() {
		return getServer().getLogger();
	}
	
	public void log(Level level, String message) {
		message = "[Conductor] " + message;
		getLogger().log(level, message);
	}
	
	public void log(String message) {
		log(Level.INFO, message);
	}

}
