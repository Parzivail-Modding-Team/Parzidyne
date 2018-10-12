package com.parzivail.util.block;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.Resources;
import net.minecraft.block.BlockLog;

public class PBlockPillar extends BlockLog
{
	public final String name;

	public PBlockPillar(String name)
	{
		this.name = name;
		setCreativeTab(Parzidyne.tab);
		setUnlocalizedName(Resources.modDot(this.name));
	}
}
