package com.edicatad.emvi.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber
public class TickHandler {
	private static long worldTime;
	public static void test(){
		/*
		 * I'm not sure this achieves what I want;  it adds the villager to the spawn list in some fashion
		 * but I'm not sure when these are actually spawned, nor do I have any control over their proximity 
		 * to the player.  I suspect a custom spawn handler may be the way to go here
		 */
		EntityRegistry.addSpawn(EntityVillager.class, 10, 0, 10, EnumCreatureType.CREATURE, Biomes.DEFAULT);
	}
	
	@SubscribeEvent
	public static void worldTick(WorldTickEvent event){
		worldTime = event.world.getWorldTime();
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
			 */
			// this is a switch in case I want to provide mod support to other mods or add villagers to the nether or something
			switch(event.world.provider.getDimension()){
			case 0:
				// We're ticking the overworld dimension!
				// using a bed sticks you at a multiple of 24000
				worldTime = event.world.getWorldTime();
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
