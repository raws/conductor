package com.raws.conductor;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.MinecartManiaPlayer;
import com.afforess.minecartmaniacore.MinecartManiaWorld;
import com.afforess.minecartmaniacore.event.MinecartCaughtEvent;
import com.afforess.minecartmaniacore.event.MinecartEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class VehicleListener extends MinecartManiaListener {
	protected final ConductorPlugin conductor;
	
	public VehicleListener(ConductorPlugin plugin) {
		this.conductor = plugin;
	}
	
	@Override
	public void onMinecartCaughtEvent(MinecartCaughtEvent event) {
		onMinecartAtStation(event);
	}

	protected void onMinecartAtStation(MinecartEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		
		if (!minecart.isOnRails()) {
			return;
		}
		
		MinecartManiaPlayer player = null;
		String destination = null;
		
		if (minecart.getPlayerPassenger() != null) {
			player = MinecartManiaWorld.getMinecartManiaPlayer(minecart.getPlayerPassenger());
			destination = conductor.getDestinationFor(player);
		}
		
		if (destination == null) {
			// Since the minecart's passenger has no destination, does the owner?
			
			Object owner = minecart.getOwner();
			if (!(owner instanceof Player)) {
				return;
			}
			
			player = MinecartManiaWorld.getMinecartManiaPlayer((Player)owner);
			destination = conductor.getDestinationFor(player);
		}
		
		if (destination == null || player == null) {
			return;
		}
		
		// Replace whitespace in destination search query with a regex
		// pattern matching anything, for a more lenient search.
		String destinationPattern = destination.replaceAll("\\s+", ".*");
		
		ArrayList<Sign> signs = SignUtils.getAdjacentSignList(minecart, 2);
		for (Sign sign : signs) {
			String lines = StringUtils.join(sign.getLines(), 0, "\n");
			String regex = "line(.*" + destinationPattern + ".*)";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher matcher = pattern.matcher(lines);
			
			if (matcher.find()) {
				minecart.stopCart();
				minecart.kill(true);
				
				String stationName = matcher.group(1).trim().replaceAll("[\r\n]+", " ");
				String message = (minecart.getOwner() == minecart.getPlayerPassenger() ? "You've" : "Your minecart has") + " arrived at " + stationName + "!";
				conductor.sendMessageTo(player, message);
				conductor.clearDestinationFor(player);
				
				break;
			}
		}
		
		event.setActionTaken(true);
	}
	
	protected void log(Level level, String message) {
		conductor.log(level, message);
	}
	
	protected void log(String message) {
		conductor.log(message);
	}
}
