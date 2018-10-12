package com.parzivail.parzidyne.handler;

import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Created by colby on 9/30/2016.
 */
public class KeyHandler
{
	public static void onInput(KeyInputEvent event)
	{
		onInput();
	}

	public static void onInput(MouseInputEvent event)
	{
		onInput();
	}

	private static void onInput()
	{
	}

	private static boolean $(KeyBinding key)
	{
		return Keyboard.isKeyDown(key.getKeyCode());
	}
}
