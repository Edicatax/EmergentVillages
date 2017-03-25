package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class TickHandler {
	private static long worldTime;
	/* Minecraft runs 10 ticks per second, so 100 ticks is ten seconds. */
	private static final int tickSpeed = 500;
	
	private static int playerToTick;
	
	/**
	 * This sets the next player to tick when Emergent Villager ticks. <br>
	 * <br>
	 * This has no return value.
	 **/
	public static void setPlayerToTick(int index){
		playerToTick = index;
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		// If I don't have this code everything runs twice per tick
		if(event.phase == Phase.END){return;}
		// The world time ticks up indefinitely so we mod it to get the time within a day
		worldTime = Math.floorMod(event.world.getWorldTime(),24000);
		// I want my code to run as little as possible for speed.  This runs the code once every (ticks/10) seconds.
		if(Math.floorMod(worldTime, tickSpeed) != 0){
			return;
		}
		
		/*
		 * TODO 
		 * spawn villagers based on random chance
		 * limit villagers spawned on a per-chunk basis
		 */
		// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
		switch(event.world.provider.getDimension()){
		case 0:
			// We're ticking the overworld dimension!
			// event.world.playerEntities.parallelStream().forEach((a)->Logger.getLogger("EMVI").log(Level.INFO,String.format("Player in world: %s", a.getDisplayNameString())));
			NBTDataHandler.printNBTTagContentsForDimension(event.world.provider.getDimension());
			if(event.world.playerEntities.size() > 0 && playerToTick < event.world.playerEntities.size()){
				LogManager.getLogger().log(Level.INFO, "Doing tick event for player at index " + playerToTick + ": " + event.world.playerEntities.get(playerToTick).getName());
				
				if(event.world.playerEntities.size() - 1 > playerToTick){ playerToTick++; }
				else 													{ playerToTick = 0; }
			}
			
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
			break;
		default:
			break;
		}
	}
}
