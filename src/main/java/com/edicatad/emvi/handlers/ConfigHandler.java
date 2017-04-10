package com.edicatad.emvi.handlers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigHandler {
	public static void handleConfig(Configuration config){
		Property current;
		int chunkRange; double maxSpawnTime; boolean logging;
		config.load();
		current = config.get(Configuration.CATEGORY_GENERAL, "Logging", false);
		current.setComment("Enable this to receive server messages whenever a villager tries to spawn.  Default false.");
		logging = current.getBoolean();
		current = config.get("SpawnValues", "Chunk check range", 2);
		current.setComment("This is the range in chunks from each player that Emergent Villages checks for valid spawn positions.  Default 2.");
		chunkRange = current.getInt();
		current = config.get("SpawnValues", "Inhabited time for maximum spawn chance", 3600000.0d);
		current.setComment("This is the time in ticks at which the spawn chance for a villager for any given chunk is 100%.  Minecraft increments this timer for each player "
				+ "in a chunk once per tick.  Increase this value for a slower spawn rate, and decrease it for a faster spawn rate.  "
				+ "Default 3600000.0f.");
		maxSpawnTime = current.getDouble();
		current = config.get(Configuration.CATEGORY_GENERAL, "Max villagers per chunk", 1);
		current.setComment("This is the maximum amount of villagers that Emergent Villages spawns per chunk.  Default 1.");
		SpawnHandler.initConfig(chunkRange, current.getInt(), maxSpawnTime, logging);
		String[] dimensionArray = {"0"};
		current = config.get(Configuration.CATEGORY_GENERAL , "Dimensions", dimensionArray);
		current.setComment("These are the dimensions that Emergent Villages will spawn villagers in.  Default 0 (Overworld).");
		dimensionArray = current.getStringList();
		current = config.get(Configuration.CATEGORY_GENERAL, "Tick speed", 600);
		current.setComment("This is the amount of time that Emergent Villages waits between checks.  Minecraft ticks 10 times per second.  Higher numbers means that even if "
				+ "the regional difficulty is high it will take a while to spawn villagers, but the impact on the server will be low.  Lower numbers means villagers spawn "
				+ "faster, up to the limit, but there will be a performance hit.  Default 600.");
		TickHandler.initConfig(current.getInt(), dimensionArray);
		config.save();
	}
}
