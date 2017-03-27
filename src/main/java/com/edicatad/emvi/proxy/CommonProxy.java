package com.edicatad.emvi.proxy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.edicatad.emvi.handlers.NBTDataHandler;
import com.edicatad.emvi.handlers.TickHandler;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class CommonProxy implements IProxy{
	// This code runs server-side
	// Remember that Minecraft singleplayer loads a local server in the background
	@Override
	public void preInit() {
		// TODO Auto-generated method stub
		LogManager.getLogger().log(Level.WARN, "preInit");
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		LogManager.getLogger().log(Level.WARN, "Init");
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		LogManager.getLogger().log(Level.WARN, "postInit");
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
