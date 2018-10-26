package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.item.ItemDebugWizard;
import com.parzivail.parzidyne.item.PItem;
import com.parzivail.parzidyne.item.PItemFood;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by colby on 12/26/2017.
 */
public class ItemRegister
{
	public static PItem debugWizard;

	public static PItem carbonFiber;
	public static PItem carbonFiberComposite;
	public static PItem carbonFiberMesh;

	public static PItem scrap;

	public static PItem magnesiumSalt;

	public static PItem tungsten;
	public static PItem magnesium;

	public static PItem tungstenMagnesiumAlloy;
	public static PItem tungstenCarbideAlloy;

	public static void register()
	{
		register(debugWizard = new ItemDebugWizard());

		register(carbonFiber = new PItem("carbonFiber"));
		register(carbonFiberComposite = new PItem("carbonFiberComposite"));
		register(carbonFiberMesh = new PItem("carbonFiberMesh"));

		register(scrap = new PItem("scrap"));

		register(magnesiumSalt = new PItem("magnesiumSalt"));

		register(tungsten = new PItem("tungsten"));
		register(magnesium = new PItem("magnesium"));

		register(tungstenMagnesiumAlloy = new PItem("tungstenMagnesiumAlloy"));
		register(tungstenCarbideAlloy = new PItem("tungstenCarbideAlloy"));

		OreDictionary.registerOre("ingotTungsten", tungsten);
		OreDictionary.registerOre("ingotMagnesium", magnesium);
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
