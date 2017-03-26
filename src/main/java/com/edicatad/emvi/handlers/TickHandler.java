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
	private static final int tickSpeed = 50;
	// This should be a different value for each dimension
	private static int playerToTick;
	
	/**
	 * This sets the next player to tick when Emergent Villager ticks.  If index is less than 0, playerToTick will be set to 0.<br>
	 * <br>
	 * This has no return value.
	 **/
	public static void setPlayerToTick(int index){
		playerToTick = index;
		if(playerToTick < 0){ playerToTick = 0; }
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
		 * Load stuff like tickSpeed from a config file
		 * spawn villagers based on random chance
		 * limit villagers spawned on a per-chunk basis
		 */
		// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
		switch(event.world.provider.getDimension()){
		case 0:
			// We're ticking the overworld dimension!
			NBTDataHandler.printNBTTagContentsForDimension(event.world.provider.getDimension());
			if(event.world.playerEntities.size() > 0 && playerToTick < event.world.playerEntities.size()){
				LogManager.getLogger().log(Level.INFO, "Attempting spawn for player at index " + playerToTick + ": " + event.world.playerEntities.get(playerToTick).getName());
				SpawnHandler.attemptSpawnNearPlayer(event.world.playerEntities.get(playerToTick), event.world);
				if(event.world.playerEntities.size() - 1 > playerToTick){ playerToTick++; }
				else 													{ playerToTick = 0; }
			}
			break;
		default:
			break;
		}
	}
}
