package com.parzivail.parzidyne.network;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.network.transaction.TransactionToggleQuarry;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class TransactionBroker
{
	private static final HashMap<String, Class<? extends Transaction>> transactions = new HashMap<>();

	static
	{
		register(TransactionToggleQuarry.class);
	}

	private static void register(Class<? extends Transaction> t)
	{
		transactions.put(t.getSimpleName(), t);
	}

	public static Transaction transactionFor(String opcode)
	{
		Class<? extends Transaction> clazz = transactions.get(opcode);
		try
		{
			return clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			// This should never happen
			e.printStackTrace();
			return null;
		}
	}

	public static void dispatch(Transaction t)
	{
		Parzidyne.network.sendToServer(new MessageTransaction(t));
	}

	public static void dispatch(Transaction... t)
	{
		for (Transaction _t : t)
			dispatch(_t);
	}

	public static void consume(String opcode, NBTTagCompound data)
	{
		Transaction t = transactionFor(opcode);
		if (t == null)
			return;

		t.deserialize(data);
		t.handle();
	}
}
