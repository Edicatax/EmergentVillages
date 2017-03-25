package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static final String tagName = "EmVi";
	
	@SubscribeEvent
	public static void entitySpawned(EntityJoinWorldEvent event){
		if(event.getEntity().getName().contains("Villager")
				&& !event.getEntity().getName().contains("Zombie")
				&& !event.getWorld().isRemote 
				&& !event.getEntity().getEntityData().getBoolean(tagName)){
			LogManager.getLogger().log(Level.INFO, "Villager spawned at chunk coords: x " + MathHelper.floor(event.getEntity().posX / 16.0D) + ", z " + MathHelper.floor(event.getEntity().posZ / 16.0D));
			// set this to true to inform future calls of this code that this villager has been handled.
			event.getEntity().getEntityData().setBoolean(tagName, true);
			// have to use the MathHelper workaround because chunkX and chunkZ are 0 on spawn
			NBTDataHandler.incrementVillagersSpawnedForChunk(event.getWorld().provider.getDimension(), MathHelper.floor(event.getEntity().posX / 16.0D), MathHelper.floor(event.getEntity().posZ / 16.0D));
		}
	}
}
