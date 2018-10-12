package com.parzivail.parzidyne.item;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.Resources;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class PItemFood extends ItemFood
{
	public final String name;

	public PItemFood(String name, int food, float sat)
	{
		super(food, sat, false);
		this.name = name;
		setCreativeTab(Parzidyne.tab);
		setTextureName(Resources.modColon(this.name));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return Resources.itemDot(name);
	}
}
