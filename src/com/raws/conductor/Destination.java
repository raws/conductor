package com.raws.conductor;

import com.afforess.minecartmaniacore.MinecartManiaPlayer;

public class Destination {
	
	private MinecartManiaPlayer player;
	private String station;
	private boolean persist;
	
	public Destination(MinecartManiaPlayer player, String station, boolean persist) {
		this.player = player;
		this.station = station;
		this.persist = persist;
	}
	
	public Destination(MinecartManiaPlayer player, String destination) {
		this.player = player;
		this.station = destination;
		this.persist = false;
	}

	public String getStation() {
		return station;
	}

	public boolean shouldPersist() {
		return persist;
	}

	public MinecartManiaPlayer getPlayer() {
		return player;
	}
	
}
