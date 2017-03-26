package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static final String tagName = "EmVi";
	
	private static final int chunkCheckRange = 2;
	
	/**
	 * Attempts to spawn a villager near the specified player.  It looks at a random chunk within 2 chunks of the player and then spawns a villager if a random value exceeds the local difficulty
	 * of that chunk. <br>
	 * @param player
	 * @param world
	 */
	public static void attemptSpawnNearPlayer(EntityPlayer player, World world){
		int chunkX = MathHelper.floor( player.posX / 16.0D ) + ( (int) Math.round( Math.random() * ( 2 * chunkCheckRange ) ) - chunkCheckRange );
		int chunkZ = MathHelper.floor( player.posZ / 16.0D ) + ( (int) Math.round( Math.random() * ( 2 * chunkCheckRange ) ) - chunkCheckRange );
		// TODO check here if this chunk already had a villager spawned in it.
		// getClampedAdditionalDifficulty returns a value between 0 and 1 based on the time spent in the chunk.  I could use getDifficultyForLocation here but I'd have to convert
		// chunk coordinates to world coordinates and I don't want to do unnecessary calculations.
		float clampedChunkDifficulty = new DifficultyInstance(world.getDifficulty(), world.getWorldTime(), world.getChunkFromChunkCoords(chunkX, chunkZ).getInhabitedTime(), world.getMoonPhase()).getClampedAdditionalDifficulty();
		if(Math.random() < clampedChunkDifficulty){
			// We've hit paydirt charles!
			int lowerBoundX = chunkX * 16;
			int lowerBoundZ = chunkZ * 16;
			int upperBoundX = 16;
			int upperBoundZ = 16;
			// Grab random in-chunk coordinates
	        int worldX = lowerBoundX + (int)Math.round(Math.random() * upperBoundX);
	        int worldZ = lowerBoundZ + (int)Math.round(Math.random() * upperBoundZ);
	        int initialWorldX = worldX;
	        int initialWorldZ = worldZ;
	        boolean flag = false;
	        // Make up to four attempts to spawn a villager
	        for(int i = 0;!flag && i < 4;++i){
	        	// Attempt to spawn a villager here:
                BlockPos blockpos = world.getTopSolidOrLiquidBlock(new BlockPos(worldX, 0, worldZ));
                if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, blockpos))
                {
                    EntityVillager entityVillager;

        			LogManager.getLogger().log(Level.WARN, "Trying to spawn a villager: x = " + worldZ + ", z = " + worldX);
                    try
                    {
        				entityVillager = new EntityVillager(world);
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                        continue;
                    }

                    entityVillager.setLocationAndAngles((double)worldX + 0.5D, (double)blockpos.getY(), (double)worldZ + 0.5D, 0.0F, 0.0F);
                    // we don't need to use setRandomProfession because that gets called on finalizeMobSpawn anyway
                    //VillagerRegistry.setRandomProfession(entityVillager, world.rand);
                    //entityVillager.setProfession(Village.chooseForgeProfession(i, entityVillager.getProfessionForge()));
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
