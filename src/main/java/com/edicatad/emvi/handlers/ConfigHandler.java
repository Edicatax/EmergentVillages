package com.edicatad.emvi.handlers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigHandler {
	public static void handleConfig(Configuration config){
		Property current;
		int container;
		config.load();
		// chunkCheckRange, maxVillagersPerChunk, tickSpeed
		current = config.get(Configuration.CATEGORY_GENERAL, "Chunk check range", 2);
		current.setComment("This is the range in chunks from each player that Emergent Villages checks for valid spawn positions.  Default 2.");
		container = current.getInt();
		current = config.get(Configuration.CATEGORY_GENERAL, "Max villagers per chunk", 1);
		current.setComment("This is the maximum amount of villagers that Emergent Villages spawns per chunk.  Default 1.");
		SpawnHandler.initConfig(container, current.getInt());
		current = config.get(Configuration.CATEGORY_GENERAL, "Tick speed", 600);
		current.setComment("This is the amount of time that Emergent Villages waits between checks.  Minecraft ticks 10 times per second.  Higher numbers means that even if "
				+ "the regional difficulty is high it will take a while to spawn villagers, but the impact on the server will be low.  Lower numbers means villagers spawn "
				+ "faster, up to the limit, but there will be a performance hit.  Default 600.");
		TickHandler.initConfig(current.getInt());
		config.save();
	}
}
