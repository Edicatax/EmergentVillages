package com.edicatad.emvi.proxy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import com.edicatad.emvi.handlers.NBTDataHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

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
		LogManager.getLogger().log(Level.WARN, "Server started!");
		for(WorldServer srv : MinecraftServer.worlds){
			NBTDataHandler.init(MinecraftServer.getServer().worlds);
		}
	}
}
