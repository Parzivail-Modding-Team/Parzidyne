package com.parzivail.parzidyne.handler;

import com.parzivail.parzidyne.proxy.Client;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

/**
 * Created by colby on 9/13/2017.
 */
public class EventHandler
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void on(TickEvent.RenderTickEvent event)
	{
		if (event.phase != TickEvent.Phase.START)
			return;

		Client.renderPartialTicks = event.renderTickTime;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void on(InputEvent.KeyInputEvent event)
	{
		KeyHandler.onInput(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void on(InputEvent.MouseInputEvent event)
	{
		KeyHandler.onInput(event);
	}

	@SubscribeEvent
	public void populate(PopulateChunkEvent.Post event)
	{
		final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkZ, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);
	}
}
