package com.parzivail.parzidyne.block;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.tile.TileQuarry;
import com.parzivail.util.block.PBlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockQuarry extends PBlockContainer
{
	public BlockQuarry()
	{
		super("quarry");
		setCreativeTab(Parzidyne.tab);
		withPlaceholderTexture();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileQuarry();
	}

	@Override
	public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
	{
		if (worldIn.isRemote)
			return true;
		else
		{
			TileQuarry tile = (TileQuarry)worldIn.getTileEntity(x, y, z);

			if (tile != null)
				player.openGui(Parzidyne.instance, Resources.GUI_QUARRY, worldIn, x, y, z);

			return true;
		}
	}
}
