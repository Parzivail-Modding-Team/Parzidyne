package com.parzivail.parzidyne.gui;

import com.parzivail.parzidyne.tile.TileCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerCompressor extends Container
{
	private final InventoryPlayer inventoryPlayer;
	private final TileCompressor tile;

	public ContainerCompressor(InventoryPlayer inventoryPlayer, TileCompressor tile)
	{
		this.inventoryPlayer = inventoryPlayer;
		this.tile = tile;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}
}
