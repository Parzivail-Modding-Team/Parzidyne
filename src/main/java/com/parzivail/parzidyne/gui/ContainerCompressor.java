package com.parzivail.parzidyne.gui;

import com.parzivail.parzidyne.tile.TileCompressor;
import com.parzivail.util.ui.SpecificSlot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCompressor extends Container
{
	private final InventoryPlayer inventoryPlayer;
	private final TileCompressor tile;
	private int lastProgress;

	public ContainerCompressor(InventoryPlayer inventoryPlayer, TileCompressor tile)
	{
		this.inventoryPlayer = inventoryPlayer;
		this.tile = tile;

		this.addSlotToContainer(new Slot(tile, 0, 39, 40));
		this.addSlotToContainer(new SpecificSlot(tile, 1, 120, 40, itemStack -> false));
		int i;

		for (i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 97 + i * 18));

		for (i = 0; i < 9; ++i)
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 155));
	}

	public void onCraftGuiOpened(ICrafting p_75132_1_)
	{
		super.onCraftGuiOpened(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, this.tile.progress);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.lastProgress != this.tile.progress)
				icrafting.sendProgressBarUpdate(this, 0, this.tile.progress);
		}

		this.lastProgress = this.tile.progress;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int phase, int value)
	{
		switch (phase)
		{
			case 0:
				this.tile.progress = value;
				break;
		}
	}

	public boolean canInteractWith(EntityPlayer player)
	{
		return this.tile.isUseableByPlayer(player);
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 1)
			{
				if (!this.mergeItemStack(itemstack1, 2, 38, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0)
			{
				if (index < 38 && !this.mergeItemStack(itemstack1, 0, 1, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 2, 38, false))
			{
				return null;
			}

			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();

			if (itemstack1.stackSize == itemstack.stackSize)
				return null;

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
}
