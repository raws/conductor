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
			// Player would like to set her destination
			String destination = StringUtils.join(args, 0, " ");
			// TODO Search through known destinations for possible matches
			setDestinationFor(player, destination);
			sendMessageTo(player, "You will disembark at " + destination + "!");
		} else {
			// Player would like to clear her destination
			clearDestinationFor(player);
			sendMessageTo(player, "You no longer have a destination.");
		}
		
		return true;
	}
	
	/**
	 * Set a player's Conductor destination.
	 * 
	 * The player's destination is a search query. Signs adjacent to stations will
	 * be searched for a line matching the regular expression <code>/^.*line$/i</code>,
	 * then searched for additional text matching the individual words of the
	 * player's destination. So, for instance, a destination of <code>gaf</code>
	 * would match a sign reading <code>Orange Line\nFort Gafley</code>.
	 * 
	 * @param player      player whose destination to set
	 * @param destination string loosely matching the player's target destination
	 */
	public void setDestinationFor(Player player, String destination) {
		destinations.put(player, destination);
	}
	
	/**
	 * Get a player's Conductor destination, if any.
	 * 
	 * See {@link #setDestinationFor(Player, String)} for a description of
	 * player destinations.
	 * 
	 * @param player player whose destination to get
	 * @return player's destination or <code>null</code>
	 */
	public String getDestinationFor(Player player) {
		return destinations.get(player);
	}
	
	/**
	 * Remove a player's Conductor destination, if any.
	 * 
	 * See {@link #setDestinationFor(Player, String)} for a description of
	 * player destinations.
	 * 
	 * @param player player whose destination to remove
	 */
	public void clearDestinationFor(Player player) {
		destinations.remove(player);
	}
	
	/**
	 * Send a Conductor-branded message to a player.
	 * 
	 * @param player  player to whom to send the message
	 * @param message message to send
	 */
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
