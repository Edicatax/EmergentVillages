package com.edicatad.emvi.handlers;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

class TickTracker {
	public int worldToTick = 0;
	public int playerToTick = 0;
	public boolean hasTicked = true;
	public TickTracker(int dimensionID, int playerIndex){
		worldToTick = dimensionID;
		playerToTick = playerIndex;
	}
	/**
	 * Increments the playerToTick counter, rolling over to 0.  Returns if the world passed is not the world it is tracking.
	 * @param world
	 */
	public void setNextPlayerToTick(World world){
		if(world.provider.getDimension() != worldToTick){return;}
		if(world.playerEntities.size() - 1 > playerToTick){ playerToTick++; }
		else { playerToTick = 0; }
	}
	public boolean trackedWorldHasPlayers(){
		if(DimensionManager.getWorld(worldToTick) == null){ return false; }
		return (!DimensionManager.getWorld(worldToTick).playerEntities.isEmpty());
	}
}

@Mod.EventBusSubscriber
public class TickHandler {
	private static long worldTime;
	// TODO Load stuff like tickSpeed from a config file
	/* Minecraft runs 10 ticks per second, so 100 ticks is ten seconds. */
	private static int tickSpeed = 600;
	private static ArrayList<TickTracker> tickList = new ArrayList<TickTracker>();
	private static int nextWorldToTick = 0;
	private static long lastTickProcessed = 0;
	
	public static void init(World world){
		tickList.add(new TickTracker(world.provider.getDimension(),0));
	}
	
	/**
	 * Initializes default values.  This is called by ConfigHandler.
	 * @param tickTime
	 */
	public static void initConfig(int tickTime){
		tickSpeed = tickTime;
	}
	
	/**
	 * @param dimensionID
	 * @return The TickTracker for the requested dimension, creating one if it doesn't exist.
	 */
	private static TickTracker getTickTrackerForDimension(int dimensionID){
		for(TickTracker track : tickList){
			if(track.worldToTick == dimensionID){return track;}
		}
		TickTracker ret = new TickTracker(dimensionID,0);
		tickList.add(ret);
		return ret;
	}
	/**
	 * Sets nextWorldToTick to the next world that we haven't ticked in this round, that has players in it.  If no such world exists it resets all tick
	 * flags and defaults to ticking the overworld.
	 */
	public static void findNextWorldToTick(){
		nextWorldToTick = 0;
		for(TickTracker track : tickList){
			if(!track.hasTicked && track.trackedWorldHasPlayers()){
				nextWorldToTick = track.worldToTick;
				return;
			}
		}
		for(TickTracker track : tickList){
			track.hasTicked = false;
		}
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		// I only want to tick on the server
		if(event.world.isRemote){return;}
		// If I don't have this code everything runs twice per tick, and we can use it to mark the tick as completed
		if(event.phase == Phase.END){return;}
		// The world time ticks up indefinitely so we mod it to get the time within a day
		worldTime = Math.floorMod(event.world.getWorldTime(),24000);
		if(Math.floorMod(worldTime, tickSpeed) != 0){return;}
		TickTracker tickTracker = getTickTrackerForDimension(event.world.provider.getDimension());
		if(!tickTracker.trackedWorldHasPlayers()){
			if(tickTracker.worldToTick == nextWorldToTick){
				tickTracker.hasTicked = true;
				findNextWorldToTick();
			}
			return;
		}
		if(!tickTracker.hasTicked && tickTracker.worldToTick == nextWorldToTick && event.world.getTotalWorldTime() > lastTickProcessed){
			// TODO dynamically go through this switch based on config somewhere
			// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
			switch(tickTracker.worldToTick){
			case 0:
				if(tickTracker.trackedWorldHasPlayers() && tickTracker.playerToTick < event.world.playerEntities.size()){
					SpawnHandler.attemptSpawnNearPlayer(event.world.playerEntities.get(tickTracker.playerToTick), event.world);
					tickTracker.setNextPlayerToTick(event.world);
				}
				break;
			default:
				break;
			}
			tickTracker.hasTicked = true;
			findNextWorldToTick();
			lastTickProcessed = event.world.getTotalWorldTime();
			return;
		}
		// if we get to this point, a world has probably unloaded and eaten our tick tracker, so find a new world to tick on
		if(DimensionManager.getWorld(nextWorldToTick) == null){
			LogManager.getLogger().log(Level.WARN, "Tracker lost, generating new tracker.");
			findNextWorldToTick();
		}
	}
}
