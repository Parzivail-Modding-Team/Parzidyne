package com.parzivail.util.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.parzivail.util.block.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.List;
import java.util.Set;

// TODO Legacy methods, replace with something sane
public class ItemDistribution
{
	public static boolean insertItemIntoInventory(IInventory inventory, ItemStack stack)
	{
		return insertItemIntoInventory(inventory, stack, ForgeDirection.UNKNOWN, -1);
	}

	public static boolean insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot)
	{
		return insertItemIntoInventory(inventory, stack, side, intoSlot, true);
	}

	public static boolean insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove)
	{
		return insertItemIntoInventory(inventory, stack, side, intoSlot, doMove, true);
	}

	public static int moveItemInto(IInventory fromInventory, int fromSlot, CustomSinks.ICustomSink sink, int maxAmount, ForgeDirection direction, boolean doMove)
	{
		fromInventory = getInventory(fromInventory);

		ItemStack sourceStack = fromInventory.getStackInSlot(fromSlot);
		if (sourceStack == null || maxAmount <= 0)
			return 0;

		if (fromInventory instanceof ISidedInventory && !((ISidedInventory)fromInventory).canExtractItem(fromSlot, sourceStack, direction.ordinal()))
			return 0;

		final int amountToMove = Math.min(sourceStack.stackSize, maxAmount);
		ItemStack clonedSourceStack = copyAndChange(sourceStack, amountToMove);

		final int inserted = sink.accept(clonedSourceStack, doMove, direction);
		if (doMove)
			removeFromSlot(fromInventory, fromSlot, inserted);
		return inserted;
	}

	public static boolean insertItemInto(ItemStack stack, CustomSinks.ICustomSink sink, ForgeDirection intoSide, boolean doMove)
	{
		ItemStack clonedSourceStack = stack.copy();

		final int inserted = sink.accept(clonedSourceStack, doMove, intoSide);

		if (inserted > 0)
		{
			stack.stackSize -= inserted;
			return true;
		}

		return false;
	}

	public static boolean insertItemIntoInventory(IInventory inventory, ItemStack stack, ForgeDirection side, int intoSlot, boolean doMove, boolean canStack)
	{
		if (stack == null)
			return false;

		final int sideId = side.ordinal();
		IInventory targetInventory = inventory;

		// if we're not meant to move, make a clone of the inventory
		if (!doMove)
		{
			GenericInventory copy = new GenericInventory("temporary.inventory", false, targetInventory.getSizeInventory());
			copy.copyFrom(inventory);
			targetInventory = copy;
		}

		final Set<Integer> attemptSlots = Sets.newTreeSet();

		// if it's a sided inventory, get all the accessible slots
		final boolean isSidedInventory = inventory instanceof ISidedInventory && side != ForgeDirection.UNKNOWN;

		if (isSidedInventory)
		{
			int[] accessibleSlots = ((ISidedInventory)inventory).getSlotsForFace(sideId);
			for (int slot : accessibleSlots)
				attemptSlots.add(slot);
		}
		else
		{
			// if it's just a standard inventory, get all slots
			for (int a = 0; a < inventory.getSizeInventory(); a++)
			{
				attemptSlots.add(a);
			}
		}

		if (intoSlot > -1)
			attemptSlots.retainAll(ImmutableSet.of(intoSlot));

		if (attemptSlots.isEmpty())
			return false;

		boolean result = false;
		for (Integer slot : attemptSlots)
		{
			if (stack.stackSize <= 0)
				break;
			if (isSidedInventory && !((ISidedInventory)inventory).canInsertItem(slot, stack, sideId))
				continue;
			result |= tryInsertStack(targetInventory, slot, stack, canStack);
		}

		return result;
	}

	public static int moveItemInto(IInventory fromInventory, int fromSlot, IInventory target, int intoSlot, int maxAmount, ForgeDirection direction, boolean doMove)
	{
		return moveItemInto(fromInventory, fromSlot, target, intoSlot, maxAmount, direction, doMove, true);
	}

	/***
	 * Move an item from the fromInventory, into the target. The target can be
	 * an inventory or pipe.
	 * Double checks are automagically wrapped. If you're not bothered what slot
	 * you insert into, pass -1 for intoSlot. If you're passing false for
	 * doMove, it'll create a dummy inventory and its calculations on that
	 * instead
	 *
	 * @param fromInventory
	 *            the inventory the item is coming from
	 * @param fromSlot
	 *            the slot the item is coming from
	 * @param toInventory
	 *            the inventory you want the item to be put into. can be BC pipe
	 *            or IInventory
	 * @param intoSlot
	 *            the target slot. Pass -1 for any slot
	 * @param maxAmount
	 *            The maximum amount you wish to pass
	 * @param direction
	 *            The direction of the move. Pass UNKNOWN if not applicable
	 * @param doMove
	 * @param canStack
	 * @return The amount of items moved
	 */
	public static int moveItemInto(IInventory fromInventory, int fromSlot, IInventory toInventory, int intoSlot, int maxAmount, ForgeDirection direction, boolean doMove, boolean canStack)
	{
		fromInventory = getInventory(fromInventory);

		ItemStack sourceStack = fromInventory.getStackInSlot(fromSlot);
		if (sourceStack == null || maxAmount <= 0)
			return 0;

		if (fromInventory instanceof ISidedInventory && !((ISidedInventory)fromInventory).canExtractItem(fromSlot, sourceStack, direction.ordinal()))
			return 0;

		final int amountToMove = Math.min(sourceStack.stackSize, maxAmount);
		ItemStack insertedStack = copyAndChange(sourceStack, amountToMove);

		IInventory targetInventory = getInventory(toInventory);
		ForgeDirection side = direction.getOpposite();
		// try insert the item into the target inventory. this'll reduce the
		// stackSize of our stack
		insertItemIntoInventory(targetInventory, insertedStack, side, intoSlot, doMove, canStack);
		int inserted = amountToMove - insertedStack.stackSize;

		if (doMove)
			removeFromSlot(fromInventory, fromSlot, inserted);

		return inserted;
	}

	private static ItemStack copyAndChange(ItemStack stack, int newSize)
	{
		ItemStack copy = stack.copy();
		copy.stackSize = newSize;
		return copy;
	}

	private static IInventory getInventory(IInventory inventory)
	{
		if (inventory instanceof TileEntityChest)
			return doubleChestFix((TileEntity)inventory);
		return inventory;
	}

	private static IInventory doubleChestFix(TileEntity te)
	{
		final World world = te.getWorld();
		final int x = te.xCoord;
		final int y = te.yCoord;
		final int z = te.zCoord;
		final Block block = te.getBlockType();
		if (world.getBlock(x - 1, y, z) == block)
			return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x - 1, y, z), (IInventory)te);
		if (world.getBlock(x + 1, y, z) == block)
			return new InventoryLargeChest("Large chest", (IInventory)te, (IInventory)world.getTileEntity(x + 1, y, z));
		if (world.getBlock(x, y, z - 1) == block)
			return new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(x, y, z - 1), (IInventory)te);
		if (world.getBlock(x, y, z + 1) == block)
			return new InventoryLargeChest("Large chest", (IInventory)te, (IInventory)world.getTileEntity(x, y, z + 1));
		return (te instanceof IInventory) ? (IInventory)te : null;
	}

	public static void removeFromSlot(IInventory inventory, int slot, int amount)
	{
		ItemStack sourceStack = inventory.getStackInSlot(slot);
		sourceStack.stackSize -= amount;
		if (sourceStack.stackSize == 0)
		{
			inventory.setInventorySlotContents(slot, null);
		}
		else
		{
			// Paranoia? Always!
			inventory.setInventorySlotContents(slot, sourceStack);
		}
	}

	public static int moveItemInto(IInventory fromInventory, int fromSlot, TileEntity te, ForgeDirection intoSide, int maxAmount, boolean doMove)
	{
		if (te == null)
			return 0;

		if (te instanceof IInventory)
		{
			final IInventory toInventory = (IInventory)te;
			final int moved = moveItemInto(fromInventory, fromSlot, toInventory, -1, maxAmount, intoSide, doMove);
			if (moved > 0)
				toInventory.markDirty(); // we are losing info here, so must commit
			return moved;
		}
		else
		{
			CustomSinks.ICustomSink adapter = CustomSinks.createSink(te);
			if (adapter != null)
				return moveItemInto(fromInventory, fromSlot, adapter, maxAmount, intoSide, doMove);
		}

		return 0;
	}

	public static boolean insertItemInto(ItemStack stack, TileEntity te, ForgeDirection intoSide, boolean doMove)
	{
		if (te == null)
			return false;

		if (te instanceof IInventory)
		{
			final IInventory toInventory = (IInventory)te;
			boolean changed = insertItemIntoInventory(toInventory, stack, intoSide, -1, doMove);
			if (changed)
				toInventory.markDirty(); // we are losing info here, so must commit
			return changed;
		}
		else
		{
			CustomSinks.ICustomSink adapter = CustomSinks.createSink(te);
			if (adapter != null)
				return insertItemInto(stack, adapter, intoSide, doMove);
		}

		return false;
	}

	public static Set<Integer> getAllSlotsWithStack(IInventory inventory, ItemStack stack)
	{
		inventory = getInventory(inventory);
		Set<Integer> slots = Sets.newHashSet();
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inventory.getStackInSlot(i);
			if (stackInSlot != null && stackInSlot.isItemEqual(stack))
				slots.add(i);
		}
		return slots;
	}

	public static Set<Integer> getAllSlots(IInventory inventory)
	{
		inventory = getInventory(inventory);
		Set<Integer> slots = Sets.newHashSet();
		for (int i = 0; i < inventory.getSizeInventory(); i++)
			slots.add(i);

		return slots;
	}

	public static int getFirstNonEmptySlot(IInventory inventory)
	{
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null)
			{
				return i;
			}
		}
		return -1;
	}

	public static int moveItemsFromOneOfSides(TileEntity te, IInventory inv, int maxAmount, int intoSlot, Iterable<ForgeDirection> sides, boolean randomize)
	{
		return moveItemsFromOneOfSides(te, inv, null, maxAmount, intoSlot, sides, randomize);
	}

	public static int moveItemsFromOneOfSides(TileEntity te, IInventory inv, ItemStack filterStack, int maxAmount, int intoSlot, Iterable<ForgeDirection> sides, boolean randomize)
	{
		if (randomize)
		{
			List<ForgeDirection> shuffledSides = Lists.newArrayList(sides);
			Collections.shuffle(shuffledSides);
			sides = shuffledSides;
		}

		IInventory ourInventory = getInventory(inv);

		// loop through the shuffled sides
		for (ForgeDirection dir : sides)
		{
			TileEntity tileOnSurface = BlockUtils.getTileInDirection(te, dir);
			// if it's an inventory
			if (tileOnSurface instanceof IInventory)
			{
				final IInventory neighbor = (IInventory)tileOnSurface;
				Set<Integer> slots = filterStack == null ? getAllSlots(neighbor) : getAllSlotsWithStack(neighbor, filterStack);
				for (Integer slot : slots)
				{
					int moved = moveItemInto(neighbor, slot, ourInventory, intoSlot, maxAmount, dir.getOpposite(), true);
					if (moved > 0)
					{
						// information is lost after leaving this method, so must commit here
						ourInventory.markDirty();
						neighbor.markDirty();
						return moved;
					}
				}
			}
		}
		return 0;
	}

	public static int moveItemsToOneOfSides(TileEntity te, IInventory inv, int fromSlot, int maxAmount, Iterable<ForgeDirection> sides, boolean randomize)
	{
		final IInventory inventory = getInventory(inv);

		// if we've not got a stack in that slot, we dont care.
		if (inventory.getStackInSlot(fromSlot) == null)
			return 0;

		// shuffle the sides that have been passed in

		if (randomize)
		{
			List<ForgeDirection> shuffledSides = Lists.newArrayList(sides);
			Collections.shuffle(shuffledSides);
			sides = shuffledSides;
		}

		for (ForgeDirection dir : sides)
		{
			// grab the tile in the current direction
			TileEntity tileOnSurface = BlockUtils.getTileInDirection(te, dir);
			int inserted = moveItemInto(inventory, fromSlot, tileOnSurface, dir, maxAmount, true);
			if (inserted > 0)
				return inserted;
		}
		return 0;
	}

	public static ItemStack removeFromFirstNonEmptySlot(IInventory invent)
	{
		int nextFilledSlot = getFirstNonEmptySlot(invent);
		if (nextFilledSlot > -1)
		{
			ItemStack copy = invent.getStackInSlot(nextFilledSlot).copy();
			invent.setInventorySlotContents(nextFilledSlot, null);
			return copy;
		}
		return null;
	}

	public static boolean areItemAndTagEqual(final ItemStack stackA, ItemStack stackB)
	{
		return stackA.isItemEqual(stackB) && ItemStack.areItemStackTagsEqual(stackA, stackB);
	}

	public static boolean areMergeCandidates(ItemStack source, ItemStack target)
	{
		return areItemAndTagEqual(source, target) && target.stackSize < target.getMaxStackSize();
	}

	/**
	 * Tests to see if an item stack can be inserted in to an inventory Does not
	 * perform the insertion, only tests the possibility
	 *
	 * @param inventory The inventory to insert the stack into
	 * @param item      the stack to insert
	 *
	 * @return the amount of items that could be put in to the stack
	 */
	public static int testInventoryInsertion(IInventory inventory, ItemStack item)
	{
		if (item == null || item.stackSize == 0)
			return 0;
		if (inventory == null)
			return 0;
		int slotCount = inventory.getSizeInventory();
		/*
		 * Allows counting down the item size, without cloning or changing the
		 * object
		 */
		int itemSizeCounter = item.stackSize;
		for (int i = 0; i < slotCount && itemSizeCounter > 0; i++)
		{

			if (!inventory.isItemValidForSlot(i, item))
				continue;
			ItemStack inventorySlot = inventory.getStackInSlot(i);
			/*
			 * If the slot is empty, dump the biggest stack we can, taking in to
			 * consideration, the remaining amount of stack
			 */
			if (inventorySlot == null)
			{
				itemSizeCounter -= Math.min(Math.min(itemSizeCounter, inventory.getInventoryStackLimit()), item.getMaxStackSize());
			}
			/* If the slot is not empty, check that these items stack */
			else if (areMergeCandidates(item, inventorySlot))
			{
				/* If they stack, decrement by the amount of space that remains */

				int space = inventorySlot.getMaxStackSize() - inventorySlot.stackSize;
				itemSizeCounter -= Math.min(itemSizeCounter, space);
			}
		}
		// itemSizeCounter might be less than zero here. It shouldn't be, but I
		// don't trust me. -NC
		if (itemSizeCounter != item.stackSize)
		{
			itemSizeCounter = Math.max(itemSizeCounter, 0);
			return item.stackSize - itemSizeCounter;
		}
		return 0;
	}

	/***
	 * Try to merge the supplied stack into the supplied slot in the target
	 * inventory
	 *
	 * @param targetInventory
	 *            Although it doesn't return anything, it'll REDUCE the stack
	 *            size of the stack that you pass in
	 *
	 * @param slot
	 * @param stack
	 */
	public static boolean tryInsertStack(IInventory targetInventory, int slot, ItemStack stack, boolean canMerge)
	{
		if (targetInventory.isItemValidForSlot(slot, stack))
		{
			ItemStack targetStack = targetInventory.getStackInSlot(slot);
			if (targetStack == null)
			{
				int limit = targetInventory.getInventoryStackLimit();
				if (limit < stack.stackSize)
				{
					targetInventory.setInventorySlotContents(slot, stack.splitStack(limit));
				}
				else
				{
					targetInventory.setInventorySlotContents(slot, stack.copy());
					stack.stackSize = 0;
				}
				return true;
			}
			else if (canMerge)
			{
				if (targetInventory.isItemValidForSlot(slot, stack) && areMergeCandidates(stack, targetStack))
				{
					int space = targetStack.getMaxStackSize() - targetStack.stackSize;
					int mergeAmount = Math.min(space, stack.stackSize);
					ItemStack copy = targetStack.copy();
					copy.stackSize += mergeAmount;
					targetInventory.setInventorySlotContents(slot, copy);
					stack.stackSize -= mergeAmount;
					return true;
				}
			}
		}
		return false;
	}
}
