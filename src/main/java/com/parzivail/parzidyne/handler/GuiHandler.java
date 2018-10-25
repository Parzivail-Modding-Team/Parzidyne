package com.parzivail.parzidyne.handler;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.container.ContainerNothing;
import com.parzivail.parzidyne.gui.ContainerCompressor;
import com.parzivail.parzidyne.gui.GuiCompressor;
import com.parzivail.parzidyne.gui.GuiQuarry;
import com.parzivail.parzidyne.tile.TileCompressor;
import com.parzivail.parzidyne.tile.TileQuarry;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == Resources.GUI_QUARRY)
			return new GuiQuarry(player.inventory, (TileQuarry)world.getTileEntity(x, y, z));
		if (id == Resources.GUI_COMPRESSOR)
			return new GuiCompressor(player.inventory, (TileCompressor)world.getTileEntity(x, y, z));
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == Resources.GUI_QUARRY)
			return new ContainerNothing();
		if (id == Resources.GUI_COMPRESSOR)
			return new ContainerCompressor(player.inventory, (TileCompressor)world.getTileEntity(x, y, z));
		return null;
	}
}
