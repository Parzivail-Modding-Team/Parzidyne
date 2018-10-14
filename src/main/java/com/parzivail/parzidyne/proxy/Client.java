package com.parzivail.parzidyne.proxy;

import com.parzivail.parzidyne.render.RenderQuarry;
import com.parzivail.parzidyne.tile.TileQuarry;
import com.parzivail.util.common.Lumberjack;
import cpw.mods.fml.client.registry.ClientRegistry;
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

		ClientRegistry.bindTileEntitySpecialRenderer(TileQuarry.class, new RenderQuarry());

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
