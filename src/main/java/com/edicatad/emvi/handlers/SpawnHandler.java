package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static final String tagName = "EmVi";
	
	/**
	 * Attempts to spawn a villager near the specified player.  It looks at a random chunk within 2 chunks of the player and then spawns a villager if a random value exceeds the local difficulty
	 * of that chunk. <br>
	 * @param player
	 * @param world
	 */
	public static void attemptSpawnNearPlayer(Entity player, World world){
		/// Spawn code from spawn egg:
		/*
		int x = 
		EntityLiving entityliving = (EntityLiving)entity;
        entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        entityliving.rotationYawHead = entityliving.rotationYaw;
        entityliving.renderYawOffset = entityliving.rotationYaw;
        entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
        worldIn.spawnEntity(entity);
        entityliving.playLivingSound();*/
	}
	
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
