package com.parzivail.parzidyne.handler;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.container.ContainerNothing;
import com.parzivail.parzidyne.gui.GuiRf;
import com.parzivail.parzidyne.tile.TileRF;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == Resources.GUI_RF)
			return new GuiRf(player.inventory, (TileRF)world.getTileEntity(x, y, z));
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == Resources.GUI_RF)
			return new ContainerNothing();
		return null;
	}
}
