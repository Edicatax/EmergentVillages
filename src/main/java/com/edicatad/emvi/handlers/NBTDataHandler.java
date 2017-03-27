package com.edicatad.emvi.handlers;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.edicatad.emvi.world.storage.VillagerData;

import net.minecraft.world.World;

public class NBTDataHandler {
	private static ArrayList<VillagerData> villagerDataList = new ArrayList<VillagerData>();
	
	private static final String tagName = "EmVi";
	
	/**
	 * Grabs the stored VillagerData for the dimension, or creates it if there is none. <br>
	 * @param world
	 */
	public static void init(World world){
		// We want to grab the dimension to support multiple dimensions
		int dimensionID = world.provider.getDimension();
		for(VillagerData data : villagerDataList){
			if(data.getDimensionID() == dimensionID){
				// if I already have a VillagerData for this dimension I don't need to do anything
				return;
			}
		} // for
		
		villagerDataList.add((VillagerData) world.getPerWorldStorage().getOrLoadData(VillagerData.class, tagName));
		// size() - 1 is the index of the last added element.  If no data is loaded we need to initialize it.
		if(villagerDataList.get(villagerDataList.size() - 1) == null){
			villagerDataList.set(villagerDataList.size() - 1, new VillagerData(tagName));
			villagerDataList.get(villagerDataList.size() - 1).setDimensionID(dimensionID);
			world.getPerWorldStorage().setData(tagName, villagerDataList.get(villagerDataList.size() - 1));
		}
	}
	
	/**
     * Gets the amount of villagers spawned in a given chunk.  <br>
     * <br>
     * Returns -1 if no data is present.
     */
	public static int getVillagersSpawnedForChunk(int dimensionID, int chunkX, int chunkZ){
		// this returns 0 if no villagers have spawned or if there is no data stored - functionally the same
		for(VillagerData data : villagerDataList){
			if(data.getDimensionID() == dimensionID){
				// We can get away with only passing the chunk coordinates because every dimension has its own dataset
				return data.getData().getInteger(String.format("x%dz%d", chunkX, chunkZ));
			}
		} // for
		return -1;
	}
	
	public static void incrementVillagersSpawnedForChunk(int dimensionID, int chunkX, int chunkZ){
		for(VillagerData data : villagerDataList){
			if(data.getDimensionID() == dimensionID){
				data.getData().setInteger(String.format("x%dz%d", chunkX, chunkZ), getVillagersSpawnedForChunk(dimensionID, chunkX, chunkZ) + 1);
				data.markDirty();
			}
		} // for
	}
	
	public static void decrementVillagersSpawnedForChunk(int dimensionID, int chunkX, int chunkZ){
		for(VillagerData data : villagerDataList){
			if(data.getDimensionID() == dimensionID){
				if(getVillagersSpawnedForChunk(dimensionID, chunkX, chunkZ) > 0){
					data.getData().setInteger(String.format("x%dz%d", chunkX, chunkZ), getVillagersSpawnedForChunk(dimensionID, chunkX, chunkZ) - 1);
					data.markDirty();
				} else {
					LogManager.getLogger().log(Level.WARN, String.format("Tried to decrement villager spawn count for dim %d chunk at x%dz%d below 0", dimensionID, chunkX, chunkZ));
				}
			}
		} // for
	}
	
	/**
	 * Debug function
	 * @param dimensionID
	 */
	public static void printNBTTagContentsForDimension(int dimensionID){
		for(VillagerData data : villagerDataList){
			if(data.getDimensionID() == dimensionID){
				LogManager.getLogger().log(Level.WARN, "VillagerData for dimension " + dimensionID + " contains the following data:");
				for(String key : data.getData().getKeySet()){
					LogManager.getLogger().log(Level.WARN, "Key: " + key + " = " + data.getData().getInteger(key));
				}
			}
		}
	}
}
