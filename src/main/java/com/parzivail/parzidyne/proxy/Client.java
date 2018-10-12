package com.parzivail.parzidyne.proxy;

import com.parzivail.util.common.Lumberjack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;

public class Client extends Common
{
	public static Minecraft mc;
	public static float renderPartialTicks;
	public static ScaledResolution resolution;

	@Override
	public void init()
	{
		mc = Minecraft.getMinecraft();

		Lumberjack.log("Client proxy loaded!");
	}

	@Override
	public void postInit()
	{
		//KeybindRegistry.registerAll();
	}

	@Override
	public boolean isServer()
	{
		return false;
	}

	@Override
	public Entity getEntityById(int dim, int id)
	{
		return Minecraft.getMinecraft().theWorld.getEntityByID(id);
	}
}
