package com.parzivail.parzidyne.tile;

import cofh.api.energy.EnergyStorage;
import net.minecraft.nbt.NBTTagCompound;

public class TileCompressor extends TileRF
{
	@Override
	public void updateEntity()
	{
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
}
