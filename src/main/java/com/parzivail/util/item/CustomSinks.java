package com.parzivail.util.item;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class CustomSinks
{
	private static final List<ICustomSinkProvider> sinkProviders = Lists.newArrayList();

	public static ICustomSink createSink(TileEntity te)
	{
		for (ICustomSinkProvider provider : sinkProviders)
		{
			ICustomSink sink = provider.create(te);
			if (sink != null)
				return sink;
		}
		return null;
	}

	public static void registerCustomSink(ICustomSinkProvider provider)
	{
		sinkProviders.add(provider);
	}

	public interface ICustomSink
	{
		int accept(ItemStack stack, boolean doInsert, ForgeDirection direction);
	}

	public interface ICustomSinkProvider
	{
		ICustomSink create(TileEntity te);
	}
}
