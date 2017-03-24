package com.edicatad.emvi.handlers;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class SpawnHandler {
	private static long worldTime;
	
	private static final String tagName = "EmVi";
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		// The world time ticks up indefinitely so we mod it to get the time within a day
		worldTime = Math.floorMod(event.world.getWorldTime(),24000);
		// I want my code to run as little as possible for speed.  Since this runs before I check the phase it runs twice per tick
		// so it should run the code once every 5 seconds
		if(Math.floorMod(worldTime, 100) != 0){
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
			// this does nothing because the world ticks globally;  I need to grab it from the players maybe?  idk.
			switch(event.world.provider.getDimension()){
			case 0:
				// We're ticking the overworld dimension!
				LogManager.getLogger().log(Level.INFO,String.format("EmVi tick at WorldTime: %d in dimension %d", worldTime, event.world.provider.getDimension()));
				// event.world.playerEntities.parallelStream().forEach((a)->Logger.getLogger("EMVI").log(Level.INFO,String.format("Player in world: %s", a.getDisplayNameString())));
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	@SubscribeEvent
	public static void entitySpawned(EntityJoinWorldEvent event){
		if(event.getEntity().getName().contains("Villager") && !event.getWorld().isRemote && !event.getEntity().getEntityData().getBoolean(tagName)){
			// have to use the MathHelper workaround because chunkX and chunkZ are 0 on spawn
			LogManager.getLogger().log(Level.INFO, "Entity spawned at chunk coords: x " + MathHelper.floor(event.getEntity().posX / 16.0D) + ", z " + MathHelper.floor(event.getEntity().posZ / 16.0D));
			// set this to true to inform future calls of this code that this villager has been handled.
			event.getEntity().getEntityData().setBoolean(tagName, true);
		}
	}
}
