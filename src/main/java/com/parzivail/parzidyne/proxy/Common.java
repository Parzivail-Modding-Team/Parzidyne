package com.parzivail.parzidyne.proxy;

import com.parzivail.parzidyne.Parzidyne;
import com.parzivail.parzidyne.network.MessageSpawnParticle;
import com.parzivail.util.common.Lumberjack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class Common
{
	public void init()
	{
		Lumberjack.log("Server proxy loaded!");
	}

	public void registerRendering()
	{
	}

	public boolean isThePlayer(EntityPlayer entity)
	{
		return false;
	}

	public boolean isServer()
	{
		return true;
	}

	public void spawnParticle(World world, String name, double x, double y, double z, double vx, double vy, double vz)
	{
		Parzidyne.network.sendToDimension(new MessageSpawnParticle(world.provider.dimensionId, name, x, y, z, vx, vy, vz), world.provider.dimensionId);
	}

	public Entity getEntityById(int dim, int id)
	{
		return MinecraftServer.getServer().worldServerForDimension(dim).getEntityByID(id);
	}

	public void postInit()
	{
	}
}
