package com.parzivail.parzidyne.tile;

import cofh.api.energy.EnergyStorage;
import com.parzivail.util.block.BlockUtils;
import com.parzivail.util.item.GenericInventory;
import com.parzivail.util.item.ItemDistribution;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;

public class TileQuarry extends TileRF
{
	private final GenericInventory inventory = new GenericInventory("dummy", false, 1);
	private boolean mining;
	private int ticksUntilNextMine;
	private int mineIndex;
	private int mineY = 255;

	public boolean isMining()
	{
		return mining;
	}

	public void setMining(boolean enable)
	{
		this.mining = enable;
		sync();
	}

	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote)
			return;

		if (mining && ticksUntilNextMine-- <= 0)
		{
			if (extractEnergy(null, 500, true) != 500)
				return;

			mineIndex++;

			if (mineY >= yCoord)
				mineY = yCoord - 1;

			if (mineIndex >= 256)
			{
				mineIndex = 0;
				mineY--;

				if (mineY <= 0)
				{
					mineY = 0;
					mining = false;
					return;
				}

				sync();
			}

			int progressiveScanIdx = mineIndex;
			if (mineY % 2 == 0)
				progressiveScanIdx = 255 - progressiveScanIdx;

			int x = progressiveScanIdx % 16;
			int z = 15 - (int)Math.floor(progressiveScanIdx / 16f);

			if (z % 2 == 0)
				x = 15 - x;

			Chunk thisChunk = this.worldObj.getChunkFromBlockCoords(this.xCoord, this.zCoord);
			if (thisChunk == null)
				return;

			int mineX = thisChunk.xPosition * 16 + x;
			int mineZ = thisChunk.zPosition * 16 + z;

			Block block = this.worldObj.getBlock(mineX, mineY, mineZ);
			int metadata = this.worldObj.getBlockMetadata(mineX, mineY, mineZ);

			if (block.getBlockHardness(worldObj, mineX, mineY, mineZ) < 0 || block == Blocks.air || block instanceof IFluidBlock || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.lava || block == Blocks.flowing_lava)
				return;

			extractEnergy(null, 500, false);

			// TODO: suck up items dropped by containers
			this.worldObj.breakBlock(mineX, mineY, mineZ, false);

			distributeDrops(mineX, mineZ, block, metadata);

			ticksUntilNextMine = 1;
		}
	}

	private void distributeDrops(int mineX, int mineZ, Block block, int metadata)
	{
		ArrayList<ItemStack> items = block.getDrops(worldObj, mineX, mineY, mineZ, metadata, 0);
		float chance = 1;
		chance = ForgeEventFactory.fireBlockHarvesting(items, worldObj, block, mineX, mineY, mineZ, metadata, 0, chance, false, null);

		for (ItemStack item : items)
			if (worldObj.rand.nextFloat() <= chance)
				putItemInInventory(item);
	}

	private void putItemInInventory(ItemStack stack)
	{
		inventory.setInventorySlotContents(0, stack);

		for (ForgeDirection output : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileOnSurface = BlockUtils.getTileInDirection(this, output);
			if (ItemDistribution.moveItemInto(inventory, 0, tileOnSurface, output, 64, true) > 0)
				return;
		}

		if (stack.stackSize > 0)
		{
			float f = worldObj.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = worldObj.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = worldObj.rand.nextFloat() * 0.8F + 0.1F;

			EntityItem entityitem = new EntityItem(worldObj, xCoord + f, yCoord + f1 + 0.5F, zCoord + f2, stack);

			entityitem.delayBeforeCanPickup = 10;

			float f3 = 0.05F;
			entityitem.motionX = (float)worldObj.rand.nextGaussian() * f3;
			entityitem.motionY = (float)worldObj.rand.nextGaussian() * f3 + 1.0F;
			entityitem.motionZ = (float)worldObj.rand.nextGaussian() * f3;
			worldObj.spawnEntityInWorld(entityitem);
		}
	}

	@Override
	protected EnergyStorage createEnergyStorage()
	{
		return new EnergyStorage(100000, 1000, 1000);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		mining = nbt.getBoolean("mining");
		mineIndex = nbt.getInteger("mineIndex");
		mineY = nbt.getInteger("mineY");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setBoolean("mining", mining);
		nbt.setInteger("mineIndex", mineIndex);
		nbt.setInteger("mineY", mineY);
	}

	public int getLayer()
	{
		return mineY;
	}
}
