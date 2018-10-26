package com.parzivail.parzidyne.tile;

import cofh.api.energy.EnergyStorage;
import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.registry.recipe.CompressorRecipes;
import com.parzivail.util.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileCompressor extends TileRF implements ISidedInventory
{
	private static final int[] slotsTop = new int[] { 0 };
	private static final int[] slotsBottom = new int[] { 1 };

	public int progress;
	private ItemStack[] items = new ItemStack[2];

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return this.items.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int slotIn)
	{
		return this.items[slotIn];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
	 * new stack.
	 */
	public ItemStack decrStackSize(int index, int count)
	{
		if (this.items[index] != null)
		{
			ItemStack itemstack;

			if (this.items[index].stackSize <= count)
			{
				itemstack = this.items[index];
				this.items[index] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.items[index].splitStack(count);

				if (this.items[index].stackSize == 0)
				{
					this.items[index] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	public ItemStack getStackInSlotOnClosing(int index)
	{
		if (this.items[index] != null)
		{
			ItemStack itemstack = this.items[index];
			this.items[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.items[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Returns the name of the inventory
	 */
	public String getInventoryName()
	{
		return Resources.guiDot("compressor.name");
	}

	/**
	 * Returns if the inventory is named
	 */
	public boolean isCustomInventoryName()
	{
		return false;
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		NBTTagList nbttaglist = compound.getTagList("Items", 10);
		this.items = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if (b0 >= 0 && b0 < this.items.length)
			{
				this.items[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		this.progress = compound.getInteger("progress");
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("progress", this.progress);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		compound.setTag("Items", nbttaglist);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
	 */
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public void updateEntity()
	{
		boolean willBeDirty = false;

		if (!this.worldObj.isRemote)
		{
			if (this.items[0] != null)
			{
				if (this.canWork())
				{
					++this.progress;

					if (this.progress == 200)
					{
						this.progress = 0;
						this.work();
						willBeDirty = true;
					}
				}
				else
				{
					this.progress = 0;
				}
			}
			else
			{
				this.progress = 0;
			}
		}

		if (willBeDirty)
		{
			this.markDirty();
		}
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
	 */
	private boolean canWork()
	{
		if (this.items[0] == null || this.extractEnergy(null, 200, true) != 200)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = CompressorRecipes.instance().getResult(this.items[0]);
			if (itemstack == null)
				return false;
			if (this.items[1] == null)
				return true;
			if (!this.items[1].isItemEqual(itemstack))
				return false;
			int result = items[1].stackSize + itemstack.stackSize;
			return result <= getInventoryStackLimit() && result <= this.items[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	 */
	public void work()
	{
		if (this.canWork())
		{
			ItemStack itemstack = CompressorRecipes.instance().getResult(this.items[0]);

			if (this.items[1] == null)
			{
				this.items[1] = itemstack.copy();
			}
			else if (ItemUtils.areStacksEqualIgnoreNbt(itemstack, items[1]))
			{
				this.items[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
			}

			--this.items[0].stackSize;

			if (this.items[0].stackSize <= 0)
			{
				this.items[0] = null;
			}

			this.extractEnergy(null, 200, false);
		}
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes with Container
	 */
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	public void openChest()
	{
	}

	public void closeChest()
	{
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index != 1;
	}

	/**
	 * param side
	 */
	public int[] getSlotsForFace(int p_94128_1_)
	{
		return p_94128_1_ == 0 ? slotsBottom : (p_94128_1_ == 1 ? slotsTop : slotsBottom);
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
	 * side
	 */
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
	{
		return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
	 * side
	 */
	public boolean canExtractItem(int slot, ItemStack item, int side)
	{
		return side != 0;
	}

	@Override
	protected EnergyStorage createEnergyStorage()
	{
		return new EnergyStorage(100000, 1000, 1000);
	}
}
