package com.edicatad.emvi.handlers;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	public static void handleConfig(Configuration config){
		config.load();
		
		config.save();
	}
}
