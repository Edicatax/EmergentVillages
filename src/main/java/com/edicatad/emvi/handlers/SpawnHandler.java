package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static final String tagName = "EmVi";
	private static int chunkCheckRange = 2;
	private static double maxChunkInhabitedTime = 3600000.0d;
	private static int maxVillagersPerChunk = 1;
	private static boolean loggingEnabled = false;
	
	/**
	 * Initializes default values.  This is called by ConfigHandler.
	 * @param checkRange
	 * @param villagersPerChunk
	 */
	public static void initConfig(int checkRange, int villagersPerChunk, double maxInhabitedTime, boolean isLoggingEnabled){
		chunkCheckRange = checkRange;
		maxVillagersPerChunk = villagersPerChunk;
		maxChunkInhabitedTime = maxInhabitedTime;
		loggingEnabled = isLoggingEnabled;
	}
	
	/**
	 * Attempts to spawn a villager near the specified player.  It looks at a random chunk within 2 chunks of the player and then spawns a villager if a random value exceeds the local difficulty
	 * of that chunk. <br>
	 * @param player
	 * @param world
	 */
	public static void attemptSpawnNearPlayer(EntityPlayer player, World world){
		int chunkX = MathHelper.floor( player.posX / 16.0D ) + ( (int) Math.round( Math.random() * ( 2 * chunkCheckRange ) ) - chunkCheckRange );
		int chunkZ = MathHelper.floor( player.posZ / 16.0D ) + ( (int) Math.round( Math.random() * ( 2 * chunkCheckRange ) ) - chunkCheckRange );
		if(NBTDataHandler.getVillagersSpawnedForChunk(world.provider.getDimension(), chunkX, chunkZ) >= maxVillagersPerChunk){
			if(loggingEnabled){
				LogManager.getLogger().log(Level.WARN, "Tried to spawn a Villager in chunk x" + chunkX + "z" + chunkZ + " but the " + 
													"maximum amount of villagers for that chunk has already been spawned.");
			}
			return;
		}
		double clampedChunkDifficulty = MathHelper.clamp(world.getChunkFromChunkCoords(chunkX, chunkZ).getInhabitedTime() / maxChunkInhabitedTime, 0.0d, 1.0d);
		// float clampedChunkDifficulty = new DifficultyInstance(world.getDifficulty(), world.getWorldTime(), world.getChunkFromChunkCoords(chunkX, chunkZ).getInhabitedTime(), world.getMoonPhase()).getClampedAdditionalDifficulty();
		if(Math.random() < clampedChunkDifficulty){
			int lowerBoundX = chunkX * 16;
			int lowerBoundZ = chunkZ * 16;
			int upperBoundX = 16;
			int upperBoundZ = 16;
			// Grab random world coordinates within our chunk
	        int worldX = lowerBoundX + (int)Math.round(Math.random() * upperBoundX);
	        int worldZ = lowerBoundZ + (int)Math.round(Math.random() * upperBoundZ);
	        int initialWorldX = worldX;
	        int initialWorldZ = worldZ;
	        boolean flag = false;
	        // Make up to four attempts to spawn a villager
	        for(int i = 0;!flag && i < 4;++i){
                BlockPos blockpos = world.getTopSolidOrLiquidBlock(new BlockPos(worldX, 0, worldZ));
                if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, blockpos)){
                    EntityVillager entityVillager;

        			if(loggingEnabled){
        				LogManager.getLogger().log(Level.WARN, "Trying to spawn a villager: x = " + worldZ + ", z = " + worldX);
        			}
                    try{
        				entityVillager = new EntityVillager(world);
                    }
                    catch (Exception exception){
                        exception.printStackTrace();
                        continue;
                    }
                    // This tells Minecraft where to put it and what profession it will have
                    entityVillager.setLocationAndAngles((double)worldX + 0.5D, (double)blockpos.getY(), (double)worldZ + 0.5D, 0.0F, 0.0F);
                    entityVillager.setProfession(entityVillager.getProfessionForge());
                    entityVillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityVillager)), (IEntityLivingData)null, false);
                    world.spawnEntity(entityVillager);
                    flag = true;
                }
				// while j < 0 or j >= 0+16 or k < 0 or k >= 0+16
				for (worldZ += (int)Math.round((Math.random() * 10)) - 5;
						worldX < lowerBoundX || worldX >= lowerBoundX + upperBoundX || worldZ < lowerBoundZ || worldZ >= lowerBoundZ + upperBoundZ;
						worldZ = initialWorldZ + (int)Math.round(Math.random() * 10) - 5)
			    {
			        worldX = initialWorldX + (int)Math.round(Math.random() * 10) - 5;
			    }
	        }
		}
	}
	
	@SubscribeEvent
	public static void entitySpawned(EntityJoinWorldEvent event){
		// This only runs server side
		if(event.getWorld().isRemote)return;
		if(EntityVillager.class.isInstance(event.getEntity())
				&& !event.getEntity().getEntityData().getBoolean(tagName)){
			if(loggingEnabled){
				LogManager.getLogger().log(Level.INFO, "A villager spawned at x" + event.getEntity().posX + "z" + event.getEntity().posZ + " and has been added to the data list.");
			}
			// set this to true to inform future calls of this code that this villager has been handled.
			event.getEntity().getEntityData().setBoolean(tagName, true);
			// have to use the MathHelper workaround because chunkX and chunkZ are 0 on spawn
			NBTDataHandler.incrementVillagersSpawnedForChunk(event.getWorld().provider.getDimension(), MathHelper.floor(event.getEntity().posX / 16.0D), MathHelper.floor(event.getEntity().posZ / 16.0D));
		}
	}
}
