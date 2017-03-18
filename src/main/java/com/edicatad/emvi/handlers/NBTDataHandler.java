package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.edicatad.emvi.world.storage.VillagerData;

import net.minecraft.world.World;

public class NBTDataHandler {
	private static VillagerData villagerData;
	
	private static final String tagName = "EmVi";
	
	public static void init(World world){
		if(villagerData == null){
			villagerData = (VillagerData) world.getPerWorldStorage().getOrLoadData(VillagerData.class, tagName);
			
			if(villagerData == null){
				villagerData = new VillagerData(tagName);
				world.getPerWorldStorage().setData(tagName, villagerData);
			}
		}
	}
	
	public static int getVillagersSpawnedForChunk(int chunkX, int chunkZ){
		// this returns 0 if no villagers have spawned or if there is no data stored - functionally the same
		return villagerData.getData().getInteger(String.format("x%iz%i", chunkX, chunkZ));
	}
	
	public static void incrementVillagersSpawnedForChunk(int chunkX, int chunkZ){
		villagerData.getData().setInteger(String.format("x%iz%i", chunkX, chunkZ), getVillagersSpawnedForChunk(chunkX, chunkZ) + 1);
	}
	
	public static void decrementVillagersSpawnedForChunk(int chunkX, int chunkZ){
		if(getVillagersSpawnedForChunk(chunkX, chunkZ) > 0){
			villagerData.getData().setInteger(String.format("x%iz%i", chunkX, chunkZ), getVillagersSpawnedForChunk(chunkX, chunkZ) - 1);
		} else {
			LogManager.getLogger().log(Level.WARN, String.format("Tried to decrement villager spawn count for chunk at x%iz%i below 0", chunkX, chunkZ));
		}
	}
}
