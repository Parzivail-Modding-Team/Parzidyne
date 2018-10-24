package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.block.BlockQuarry;
import com.parzivail.util.block.*;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Created by colby on 12/26/2017.
 */
public class BlockRegister
{
	public static PBlockContainer quarry;

	public static PBlock tungstenOre;
	public static PBlock magnesiumOre;

	public static PBlock tungstenBlock;
	public static PBlock magnesiumBlock;
	public static PBlock tungstenCarbideBlock;
	public static PBlock tungstenMagnesiumBlock;

	public static void register()
	{
		register(quarry = new BlockQuarry());

		register(tungstenOre = new PBlock("tungstenOre"));
		register(magnesiumOre = new PBlock("magnesiumOre"));

		register(tungstenBlock = new PBlock("tungstenBlock"));
		register(magnesiumBlock = new PBlock("magnesiumBlock"));
		register(tungstenCarbideBlock = new PBlock("tungstenCarbideBlock"));
		register(tungstenMagnesiumBlock = new PBlock("tungstenMagnesiumBlock"));
	}

	private static void register(PBlock item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockSand item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockLayer item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockPillar item)
	{
		GameRegistry.registerBlock(item, item.name);
	}

	private static void register(PBlockContainer item)
	{
		GameRegistry.registerBlock(item, item.name);
		GameRegistry.registerTileEntity(item.createNewTileEntity(null, 0).getClass(), Resources.tileDot(item.name));
	}
}
