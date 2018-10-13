package com.parzivail.parzidyne.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileRF extends TileEntity implements IEnergyHandler
{
	public EnergyStorage energyStorage;

	public TileRF()
	{
		energyStorage = createEnergyStorage();
	}

	protected abstract EnergyStorage createEnergyStorage();

	@Override
	public int receiveEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate)
	{
		int x = energyStorage.receiveEnergy(maxReceive, simulate);
		if (!simulate)
			sync();
		return x;
	}

	@Override
	public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate)
	{
		int x = energyStorage.extractEnergy(maxReceive, simulate);
		if (!simulate)
			sync();
		return x;
	}

	@Override
	public int getEnergyStored(ForgeDirection forgeDirection)
	{
		return energyStorage.getEnergyStored();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if (energyStorage == null)
			energyStorage = createEnergyStorage();
		energyStorage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection forgeDirection)
	{
		return energyStorage.getMaxEnergyStored();
	}

	public void sync()
	{
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection forgeDirection)
	{
		return true;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		super.onDataPacket(net, packet);
		readFromNBT(packet.getNbtCompound());
	}
}
