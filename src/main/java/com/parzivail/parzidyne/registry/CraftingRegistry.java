package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.registry.recipe.CentrifugeRecipes;
import com.parzivail.parzidyne.registry.recipe.CompressorRecipes;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class CraftingRegistry
{
	public static void register()
	{
		addCompressor(new ItemStack(ItemRegister.carbonFiberMesh), new ItemStack(ItemRegister.carbonFiber));
		//addCompressor(new ItemStack(Blocks.coal_block), new ItemStack(Items.diamond));

		GameRegistry.addShapedRecipe(new ItemStack(BlockRegister.tungstenBlock), "XXX", "XXX", "XXX", 'X', ItemRegister.tungsten);
		GameRegistry.addShapedRecipe(new ItemStack(BlockRegister.magnesiumBlock), "XXX", "XXX", "XXX", 'X', ItemRegister.magnesium);
		GameRegistry.addShapedRecipe(new ItemStack(BlockRegister.tungstenCarbideBlock), "XXX", "XXX", "XXX", 'X', ItemRegister.tungstenCarbideAlloy);
		GameRegistry.addShapedRecipe(new ItemStack(BlockRegister.tungstenMagnesiumBlock), "XXX", "XXX", "XXX", 'X', ItemRegister.tungstenMagnesiumAlloy);

		GameRegistry.addShapedRecipe(new ItemStack(ItemRegister.carbonFiberMesh), "XXX", "XXX", "XXX", 'X', ItemRegister.carbonFiberComposite);

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegister.tungsten, 9), BlockRegister.tungstenBlock);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegister.magnesium, 9), BlockRegister.magnesiumBlock);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegister.tungstenCarbideAlloy, 9), BlockRegister.tungstenCarbideBlock);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegister.tungstenMagnesiumAlloy, 9), BlockRegister.tungstenMagnesiumBlock);
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
