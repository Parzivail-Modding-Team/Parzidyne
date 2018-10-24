package com.parzivail.parzidyne.gen;

import com.parzivail.parzidyne.registry.BlockRegister;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public final class WorldGenOres implements IWorldGenerator
{
	private final WorldGenMinable oreMagnesium;
	private final WorldGenMinable oreTungsten;

	public WorldGenOres()
	{
		this.oreMagnesium = new WorldGenMinable(BlockRegister.magnesiumOre, 0, 8, Blocks.stone);
		this.oreTungsten = new WorldGenMinable(BlockRegister.tungstenOre, 0, 8, Blocks.stone);
	}

	@Override
	public void generate(final Random rand, final int chunkX, final int chunkZ, final World world, final IChunkProvider chunkGenerator, final IChunkProvider chunkProvider)
	{
		if (TerrainGen.generateOre(world, rand, oreMagnesium, chunkX, chunkZ, OreGenEvent.GenerateMinable.EventType.CUSTOM))
			genOre(world, rand, chunkX, chunkZ, 17, oreMagnesium, 0, 64);
		if (TerrainGen.generateOre(world, rand, oreTungsten, chunkX, chunkZ, OreGenEvent.GenerateMinable.EventType.CUSTOM))
			genOre(world, rand, chunkX, chunkZ, 13, oreTungsten, 0, 32);
	}

	protected void genOre(World world, Random rand, int chunkX, int chunkZ, int amount, WorldGenerator generator, int minY, int maxY)
	{
		for (int l = 0; l < amount; ++l)
		{
			int i1 = chunkX * 16 + rand.nextInt(16);
			int j1 = rand.nextInt(maxY - minY) + minY;
			int k1 = chunkZ * 16 + rand.nextInt(16);
			generator.generate(world, rand, i1, j1, k1);
		}
	}
}
