package com.parzivail.parzidyne.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		//		if (id == Resources.GUI_BLASTER_WORKBENCH)
		//			return new GuiBlasterWorkbench(player.inventory, (TileBlasterWorkbench)world.getTileEntity(x, y, z));
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		//		if (id == Resources.GUI_BLASTER_WORKBENCH)
		//			return new ContainerBlasterWorkbench(player.inventory, (TileBlasterWorkbench)world.getTileEntity(x, y, z));
		return null;
	}
}
