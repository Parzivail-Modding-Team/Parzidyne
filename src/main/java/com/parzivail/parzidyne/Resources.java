package com.parzivail.parzidyne;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

public class Resources
{
	public static final String MODID = "parzidyne";

	private static final int MAJOR = 1;
	private static final int MINOR = 0;
	private static final int PATCH = 0;

	public static final String VERSION = MAJOR + "." + MINOR + "." + PATCH;

	private static int guiIdx = 1;

	public static String itemDot(String name)
	{
		return dot("item", MODID, name);
	}

	public static String blockDot(String name)
	{
		return dot("block", MODID, name);
	}

	public static String modDot(String name)
	{
		return dot(MODID, name);
	}

	public static String modDot(String... name)
	{
		return dot(MODID, dot(name));
	}

	public static String guiDot(String name)
	{
		return dot(MODID, dot("gui", name));
	}

	public static String itemDot(String name, String variant)
	{
		return dot("item", MODID, name, variant);
	}

	public static String containerDot(String name)
	{
		return dot("container", name);
	}

	public static String tileDot(String name)
	{
		return dot("tile", name);
	}

	public static String dot(String... params)
	{
		return String.join(".", params);
	}

	public static String modColon(String name)
	{
		return String.format("%s:%s", MODID, name);
	}

	public static ResourceLocation location(String path)
	{
		return new ResourceLocation(MODID, path);
	}

	public static String getKeyName(KeyBinding keyBinding)
	{
		return GameSettings.getKeyDisplayString(keyBinding.getKeyCode());
	}
}
