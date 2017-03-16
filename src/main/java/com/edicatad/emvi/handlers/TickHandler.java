package com.edicatad.emvi.handlers;

import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class TickHandler {
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void chunkWatched(Watch event){
		FMLLog.getLogger().info("[EMVI]  Chunk watched at coords %i, %i", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void chunkUnwatched(UnWatch event){
		FMLLog.getLogger().info("[EMVI]  Chunk unwatched at coords %i, %i", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		switch(event.phase){
		case END:
			break;
		case START:
			/*
			 * TODO 
			 * figure out which dimension this world is in, I want to only spawn villagers in the overworld
			 * use getWorldTime to only run spawn checker code at dawn or something
			 * spawn villagers based on random chance
			 */
			//event.world.getWorldTime();
			break;
		default:
			break;
		}
	}
}
