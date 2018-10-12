package com.parzivail.parzidyne.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDebugWizard extends PItem
{
	public ItemDebugWizard()
	{
		super("debugWizard");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (player.isSneaking())
		{
		}

		return stack;
	}
}
