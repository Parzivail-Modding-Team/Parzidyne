package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.registry.recipe.CentrifugeRecipes;
import com.parzivail.parzidyne.registry.recipe.CompressorRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CraftingRegistry
{
	public static void register()
	{
		addCompressor(new ItemStack(ItemRegister.carbonFiberComposite, 8), new ItemStack(ItemRegister.carbonFiber));
		addCompressor(new ItemStack(Blocks.coal_block, 16), new ItemStack(Items.diamond));
	}

	private static void addCompressor(ItemStack in, ItemStack out)
	{
		CompressorRecipes.instance().addRecipe(in, out);
	}

	private static void addCentrifuge(ItemStack in, ItemStack out)
	{
		CentrifugeRecipes.instance().addRecipe(in, out);
	}
}
