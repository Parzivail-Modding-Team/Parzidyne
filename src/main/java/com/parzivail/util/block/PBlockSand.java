package com.parzivail.util.block;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.Resources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;

public class PBlockSand extends BlockFalling
{
	public final String name;

	public PBlockSand(String name)
	{
		this.name = name;
		setCreativeTab(Parzidyne.tab);
		setUnlocalizedName(Resources.modDot(this.name));
		setTextureName(Resources.modColon(name));
		setHardness(0.5F);
		setHarvestLevel("shovel", HarvestLevel.WOOD);
		setStepSound(Block.soundTypeSand);
	}
}
