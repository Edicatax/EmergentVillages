package com.edicatad.emvi.proxy;

import com.edicatad.emvi.handlers.TickHandler;

public class CommonProxy implements IProxy{
	// This code runs server-side
	// Remember that Minecraft singleplayer loads a local server in the background
	@Override
	public void preInit() {
		// TODO Auto-generated method stub
		TickHandler.init();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInit() {
		// TODO Auto-generated method stub
		
	}
}
