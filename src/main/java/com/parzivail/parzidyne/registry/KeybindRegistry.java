package com.parzivail.parzidyne.registry;

import com.parzivail.parzidyne.Resources;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeybindRegistry
{
	@SideOnly(Side.CLIENT)
	public static KeyBinding keyDebug;

	public static void registerAll()
	{
		keyDebug = registerKeybind("keyDebug", Keyboard.KEY_NONE);
	}

	private static KeyBinding registerKeybind(String keyName, int keyCode)
	{
		KeyBinding b = new KeyBinding("key." + Resources.MODID + "." + keyName, keyCode, "key." + Resources.MODID);
		ClientRegistry.registerKeyBinding(b);
		return b;
	}
}
