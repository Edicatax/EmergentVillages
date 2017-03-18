package com.edicatad.emvi.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static long worldTime;
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		// The world time ticks up indefinitely so we mod it to get the time within a day
		worldTime = Math.floorMod(event.world.getWorldTime(),24000);
		if(Math.floorMod(worldTime, 100)!=0){
			return;
		}
		
		switch(event.phase){
		case END:
			break;
		case START:
			/*
			 * TODO 
			 * use getWorldTime to only run spawn checker code at dawn or something
			 * spawn villagers based on random chance
			 * limit villagers spawned on a per-chunk basis
			 * maintain the limit in the world save file
			 */
			// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
			switch(event.world.provider.getDimension()){
			case 0:
				// We're ticking the overworld dimension!
				// using a bed sticks you at a multiple of 24000
				if(Math.floorMod(worldTime, 10)==0){
					Logger.getLogger("EMVI").log(Level.INFO,String.format("TESTING %d", worldTime));
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
}
