package com.parzivail.parzidyne.network.transaction;

import com.parzivail.parzidyne.network.Transaction;
import com.parzivail.parzidyne.tile.TileQuarry;
import com.parzivail.util.item.NbtSave;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;

public class TransactionToggleQuarry extends Transaction
{
	@NbtSave
	public int dim;
	@NbtSave
	public int x;
	@NbtSave
	public int y;
	@NbtSave
	public int z;
	@NbtSave
	public boolean enable;

	public TransactionToggleQuarry()
	{
	}

	public TransactionToggleQuarry(TileQuarry tile, boolean enable)
	{
		dim = tile.getWorld().provider.dimensionId;
		x = tile.xCoord;
		y = tile.yCoord;
		z = tile.zCoord;
		this.enable = enable;
	}

	@Override
	public void handle()
	{
		TileEntity te = DimensionManager.getWorld(dim).getTileEntity(x, y, z);
		if (!(te instanceof TileQuarry))
			return;

		((TileQuarry)te).setMining(enable);
	}
}
