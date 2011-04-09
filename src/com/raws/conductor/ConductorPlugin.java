package com.raws.conductor;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.afforess.minecartmaniacore.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class ConductorPlugin extends JavaPlugin {
	
	protected static final String MINECART_MANIA_PLUGIN_NAME = "MinecartManiaCore";
	protected static final String DESTINATION_KEY = "com.raws.conductor.destination";
	
	protected VehicleListener vehicleListener;
	
	@Override
	public void onEnable() {
		if (!getServer().getPluginManager().isPluginEnabled(MINECART_MANIA_PLUGIN_NAME)) {
			log(Level.SEVERE, "Minecart Mania Core is required. Conductor is disabled.");
			setEnabled(false);
		}
		
		if (isEnabled()) {
			vehicleListener = new VehicleListener(this);
			
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
		
		MinecartManiaPlayer player = MinecartManiaWorld.getMinecartManiaPlayer((Player)sender);
		
		if (args.length > 0) {
			// Player would like to set her destination
			String destination = StringUtils.join(args, 0, " ");
			// TODO Search through known destinations for possible matches
			// TODO Parse some sort of --persist option from the command
			boolean persist = false;
			setDestinationFor(player, destination, persist);
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
	 * @param persist     whether or not the player's destination should be cleared upon arrival
	 */
	public void setDestinationFor(MinecartManiaPlayer player, String destination, boolean persist) {
		player.setDataValue(DESTINATION_KEY, new Destination(player, destination, persist));
	}
	
	/**
	 * Get a player's Conductor destination, if any.
	 * 
	 * See {@link #setDestinationFor(Player, String, boolean)} for a
	 * description of player destinations.
	 * 
	 * @param player player whose destination to get
	 * @return player's destination or <code>null</code>
	 */
	public Destination getDestinationFor(MinecartManiaPlayer player) {
		return (Destination)player.getDataValue(DESTINATION_KEY);
	}
	
	/**
	 * Remove a player's Conductor destination, if any.
	 * 
	 * See {@link #setDestinationFor(Player, String, boolean)} for a
	 * description of player destinations.
	 * 
	 * @param player player whose destination to remove
	 */
	public void clearDestinationFor(MinecartManiaPlayer player) {
		player.setDataValue(DESTINATION_KEY, null);
	}
	
	/**
	 * Send a Conductor-branded message to a player.
	 * 
	 * @param player  player to whom to send the message
	 * @param message message to send
	 */
	public void sendMessageTo(MinecartManiaPlayer player, String message) {
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
