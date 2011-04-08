package com.raws.conductor;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.afforess.minecartmaniacore.MinecartManiaMinecart;
import com.afforess.minecartmaniacore.event.MinecartActionEvent;
import com.afforess.minecartmaniacore.event.MinecartManiaListener;
import com.afforess.minecartmaniacore.utils.SignUtils;
import com.afforess.minecartmaniacore.utils.StringUtils;

public class ConductorVehicleListener extends MinecartManiaListener {
	protected static final Material STATION_BLOCK = Material.LOG;
	
	protected final ConductorPlugin conductor;
	
	public ConductorVehicleListener(ConductorPlugin plugin) {
		this.conductor = plugin;
	}

	@Override
	public void onMinecartActionEvent(MinecartActionEvent event) {
		if (!event.isActionTaken() && isMinecartAtStation(event)) {
			onMinecartAtStation(event);
		}
	}
	
	protected void onMinecartAtStation(MinecartActionEvent event) {
		MinecartManiaMinecart minecart = event.getMinecart();
		
		if (!(minecart.hasPlayerPassenger() && minecart.isOnRails())) {
			return;
		}
		
		Player player = minecart.getPlayerPassenger();
		String destination = conductor.getDestinationFor(player);
		
		if (destination == null) {
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
				conductor.sendMessageTo(player, "You've arrived at " + stationName + "!");
				conductor.clearDestinationFor(player);
				
				break;
			}
		}
		
		event.setActionTaken(true);	
	}
	
	protected boolean isMinecartAtStation(MinecartManiaMinecart minecart) {
		return minecart.getBlockIdBeneath() == STATION_BLOCK.getId();
	}
	
	protected boolean isMinecartAtStation(MinecartActionEvent event) {
		return isMinecartAtStation(event.getMinecart());
	}
	
	protected void log(Level level, String message) {
		conductor.log(level, message);
	}
	
	protected void log(String message) {
		conductor.log(message);
	}
}
