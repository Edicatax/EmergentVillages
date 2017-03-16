package com.edicatad.emvi.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class TickHandler {
	public static void init(){
		
	}


	@SubscribeEvent(priority=EventPriority.HIGHEST, receiveCanceled=true)
	
	public static void onEvent(LivingJumpEvent event)
	{
	    // DEBUG
	    if (event.getEntity() instanceof EntityPlayer)
	    {
	        FMLLog.getLogger().info("BOING");
	    }
	        
	}


	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void chunkWatched(Watch event){
		FMLLog.getLogger().info("[EMVI]  Chunk watched at coords %i, %i", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL)
	public static void chunkUnwatched(UnWatch event){
		FMLLog.getLogger().info("[EMVI]  Chunk unwatched at coords %i, %i", event.getChunk().chunkXPos, event.getChunk().chunkZPos);
	}
}
