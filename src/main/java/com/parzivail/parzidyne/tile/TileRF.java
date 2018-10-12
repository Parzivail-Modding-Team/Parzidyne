package com.parzivail.parzidyne.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileRF extends TileEntity implements IEnergyHandler
{
	public EnergyStorage energyStorage;

	public TileRF(int capacity, int rxRate, int txRate)
	{
		energyStorage = new EnergyStorage(capacity, rxRate, txRate);
	}

	@Override
	public int receiveEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate)
	{
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection forgeDirection, int maxReceive, boolean simulate)
	{
		return energyStorage.extractEnergy(maxReceive, simulate);
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
