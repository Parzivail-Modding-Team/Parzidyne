package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.item.ItemDebugWizard;
import com.parzivail.parzidyne.item.PItem;
import com.parzivail.parzidyne.item.PItemFood;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by colby on 12/26/2017.
 */
public class ItemRegister
{
	public static PItem debugWizard;

	public static void register()
	{
		register(debugWizard = new ItemDebugWizard());
	}

	private static void register(PItem item)
	{
		GameRegistry.registerItem(item, item.name, Resources.MODID);
	}

	private static void register(PItemFood item)
	{
		GameRegistry.registerItem(item, item.name, Resources.MODID);
	}
}
