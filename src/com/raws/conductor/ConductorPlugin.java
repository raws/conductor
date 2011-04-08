package com.raws.conductor;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.utils.StringUtils;

public class ConductorPlugin extends JavaPlugin {
	
	protected static final String MINECART_MANIA_PLUGIN_NAME = "MinecartManiaCore";
	
	protected ConductorVehicleListener vehicleListener;
	protected HashMap<Player,String> destinations;
	
	@Override
	public void onEnable() {
		if (!getServer().getPluginManager().isPluginEnabled(MINECART_MANIA_PLUGIN_NAME)) {
			log(Level.SEVERE, "Minecart Mania Core is required. Conductor is disabled.");
			setEnabled(false);
		}
		
		if (isEnabled()) {
			vehicleListener = new ConductorVehicleListener(this);
			destinations = new HashMap<Player,String>();
			
			PluginManager manager = getServer().getPluginManager();
			manager.registerEvent(Event.Type.CUSTOM_EVENT, vehicleListener, Event.Priority.High, this);
			
			log("Version " + getDescription().getVersion() + " loaded.");
		}
	}

	@Override
	public void onDisable() {
		// Do nothing
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (command.getName().equalsIgnoreCase("to")) {
			return onToCommand(sender, command, commandLabel, args);
		}
		
		return false;
	}
	
	protected boolean onToCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player)sender;
		
		if (args.length > 0) {
			String destination = StringUtils.join(args, 0, " ");
			// TODO Search through known destinations for possible matches
			setDestinationFor(player, destination);
			sendMessageTo(player, "You will disembark at " + destination + "!");
		} else {
			clearDestinationFor(player);
			sendMessageTo(player, "You no longer have a destination.");
		}
		
		return true;
	}
	
	public void setDestinationFor(Player player, String destination) {
		destinations.put(player, destination);
	}
	
	public String getDestinationFor(Player player) {
		return destinations.get(player);
	}
	
	public void clearDestinationFor(Player player) {
		destinations.remove(player);
	}
	
	public void sendMessageTo(Player player, String message) {
		player.sendMessage(ChatColor.GRAY + message);
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
