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

public class ClientProxy implements IProxy{
	// This code only runs clientside
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.handleConfig(new Configuration(event.getSuggestedConfigurationFile()));
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
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
