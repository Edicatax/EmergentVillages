package com.edicatad.emvi.proxy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.edicatad.emvi.handlers.ConfigHandler;
import com.edicatad.emvi.handlers.NBTDataHandler;
import com.edicatad.emvi.handlers.TickHandler;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy implements IProxy{
	// This code runs server-side
	// Remember that Minecraft singleplayer loads a local server in the background
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// Load config stuff
		ConfigHandler.handleConfig(new Configuration(event.getSuggestedConfigurationFile()));
	}

	@Override
	public void init() {
	}

	@Override
	public void postInit() {
	}
	
	@Override
	public void serverStarted()
	{
		LogManager.getLogger().log(Level.INFO, "Hooking in Emergent Villagers' spawn logic");
		// Every dimension should have a VillagerData attached
		for(WorldServer srv : DimensionManager.getWorlds()){
			NBTDataHandler.init(srv);
			TickHandler.init(srv);
		}
		TickHandler.findNextWorldToTick();
	}
}
