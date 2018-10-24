package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.gen.WorldGenOres;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by colby on 9/10/2017.
 */
public class WorldRegister
{
	public static void register()
	{
		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);
	}
}
