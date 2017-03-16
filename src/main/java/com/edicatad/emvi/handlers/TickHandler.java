package com.edicatad.emvi.handlers;

import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
}
