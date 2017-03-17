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
		FMLLog.getLogger().info("[EMVI]  Chunk watched at coords %d, %d", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void chunkUnwatched(UnWatch event){
		FMLLog.getLogger().info("[EMVI]  Chunk unwatched at coords %d, %d", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		switch(event.phase){
		case END:
			break;
		case START:
			/*
			 * TODO 
			 * use getWorldTime to only run spawn checker code at dawn or something
			 * spawn villagers based on random chance
			 */
			//event.world.getWorldTime();
			// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
			switch(event.world.provider.getDimension()){
			case 0:
				// We're ticking the overworld dimension!
				// maybe info has trouble with non-local parameter passes, try storing the world time as an int
				FMLLog.getLogger().info("Tick %d", event.world.getWorldTime());
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
