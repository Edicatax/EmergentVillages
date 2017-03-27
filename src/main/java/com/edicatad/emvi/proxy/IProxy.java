package com.edicatad.emvi.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy {
	public void preInit(FMLPreInitializationEvent event);
	public void init();
	public void postInit();
	public void serverStarted();
}
