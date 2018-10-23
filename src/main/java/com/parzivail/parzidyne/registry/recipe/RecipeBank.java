package com.parzivail.parzidyne.registry.recipe;

import com.parzivail.util.item.ItemUtils;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecipeBank
{
	private HashMap<ItemStack, ItemStack> recipes = new HashMap<>();

	public void addRecipe(ItemStack in, ItemStack out)
	{
		recipes.put(in, out);
	}

	public ItemStack getResult(ItemStack in)
	{
		Iterator<Map.Entry<ItemStack, ItemStack>> iterator = recipes.entrySet().iterator();
		Map.Entry<ItemStack, ItemStack> entry;

		do
		{
			if (!iterator.hasNext())
				return null;

			entry = iterator.next();
		}
		while (!ItemUtils.areStacksEqualIgnoreNbt(in, entry.getKey()));

		return entry.getValue();
	}
}
