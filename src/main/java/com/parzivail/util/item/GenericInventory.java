package com.parzivail.util.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;
import java.util.List;

public class GenericInventory implements IInventory
{
	public static final String TAG_SLOT = "Slot";
	public static final String TAG_ITEMS = "Items";
	public static final String TAG_SIZE = "size";

	protected String inventoryTitle;
	protected int slotsCount;
	protected ItemStack[] inventoryContents;
	protected boolean isInvNameLocalized;

	public GenericInventory(String name, boolean isInvNameLocalized, int size)
	{
		this.isInvNameLocalized = isInvNameLocalized;
		this.slotsCount = size;
		this.inventoryTitle = name;
		this.inventoryContents = new ItemStack[size];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inventoryContents[par1] != null)
		{
			ItemStack itemstack;

			if (this.inventoryContents[par1].stackSize <= par2)
			{
				itemstack = this.inventoryContents[par1];
				this.inventoryContents[par1] = null;
				onInventoryChanged(par1);
				return itemstack;
			}
			itemstack = this.inventoryContents[par1].splitStack(par2);
			if (this.inventoryContents[par1].stackSize == 0)
			{
				this.inventoryContents[par1] = null;
			}

			onInventoryChanged(par1);
			return itemstack;
		}
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return slotsCount;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventoryContents[i];
	}

	public ItemStack getStackInSlot(Enum<?> i)
	{
		return getStackInSlot(i.ordinal());
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (i >= this.inventoryContents.length)
		{
			return null;
		}
		if (this.inventoryContents[i] != null)
		{
			ItemStack itemstack = this.inventoryContents[i];
			this.inventoryContents[i] = null;
			return itemstack;
		}
		return null;
	}

	public boolean isItem(int slot, Item item)
	{
		return inventoryContents[slot] != null && inventoryContents[slot].getItem() == item;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{

	}

	public void onInventoryChanged(int slotNumber)
	{
	}

	public void clearAndSetSlotCount(int amount)
	{
		this.slotsCount = amount;
		inventoryContents = new ItemStack[amount];
		onInventoryChanged(0);
	}

	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.hasKey(TAG_SIZE))
		{
			this.slotsCount = tag.getInteger(TAG_SIZE);
		}
		NBTTagList nbttaglist = tag.getTagList(TAG_ITEMS, 10);
		inventoryContents = new ItemStack[slotsCount];
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound stacktag = nbttaglist.getCompoundTagAt(i);
			int j = stacktag.getByte(TAG_SLOT);
			if (j >= 0 && j < inventoryContents.length)
			{
				inventoryContents[j] = ItemStack.loadItemStackFromNBT(stacktag);
			}
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventoryContents[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}

		onInventoryChanged(i);
	}

	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setInteger(TAG_SIZE, getSizeInventory());
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventoryContents.length; i++)
		{
			if (inventoryContents[i] != null)
			{
				NBTTagCompound stacktag = inventoryContents[i].writeToNBT(new NBTTagCompound());
				stacktag.setByte(TAG_SLOT, (byte)i);
				nbttaglist.appendTag(stacktag);
			}
		}
		tag.setTag(TAG_ITEMS, nbttaglist);
	}

	/**
	 * This bastard never even gets called, so don't rely on it.
	 */
	@Override
	public void markDirty()
	{
		onInventoryChanged(0);
	}

	public void copyFrom(IInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (i < getSizeInventory())
			{
				ItemStack stack = inventory.getStackInSlot(i);
				if (stack != null)
				{
					setInventorySlotContents(i, stack.copy());
				}
				else
				{
					setInventorySlotContents(i, null);
				}
			}
		}
	}

	public List<ItemStack> contents()
	{
		return Arrays.asList(inventoryContents);
	}

	@Override
	public String getInventoryName()
	{
		return this.inventoryTitle;
	}

	@Override
	public boolean isCustomInventoryName()
	{
		return this.isInvNameLocalized;
	}
}
