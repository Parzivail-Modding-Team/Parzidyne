package com.parzivail.parzidyne;

import com.parzivail.parzidyne.config.Config;
import com.parzivail.parzidyne.handler.EventHandler;
import com.parzivail.parzidyne.handler.GuiHandler;
import com.parzivail.parzidyne.network.MessageSpawnParticle;
import com.parzivail.parzidyne.network.MessageTransaction;
import com.parzivail.parzidyne.proxy.Common;
import com.parzivail.parzidyne.registry.*;
import com.parzivail.parzidyne.tab.PTab;
import com.parzivail.util.common.Lumberjack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.util.vector.Vector3f;

import java.io.File;
import java.util.Random;

@Mod(modid = Resources.MODID, version = Resources.VERSION)
public class Parzidyne
{
	@SidedProxy(clientSide = "com.parzivail.parzidyne.proxy.Client", serverSide = "com.parzivail.parzidyne.proxy.Common")
	public static Common proxy;

	@Mod.Instance(Resources.MODID)
	public static Parzidyne instance;

	public static Config config;
	public static File configDir;

	public static CreativeTabs tab;

	public static EventHandler eventHandler;
	public static Random random = new Random();

	public static SimpleNetworkWrapper network;
	private static int packetId;

	public Vector3f traceStart = new Vector3f(0, 0, 0);
	public Vector3f traceEnd = new Vector3f(0, 0, 0);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configDir = event.getModConfigurationDirectory();
		config = new Config(event.getSuggestedConfigurationFile());
		setupNetworking();
	}

	private void setupNetworking()
	{
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Resources.MODID + "." + "chan");

		registerMessageServer(MessageTransaction.class);

		registerMessageClient(MessageSpawnParticle.class);
	}

	@SuppressWarnings("unchecked")
	private void registerMessageServer(Class messageHandler)
	{
		network.registerMessage(messageHandler, messageHandler, packetId, Side.SERVER);
		Lumberjack.debug("Registered server packet \"" + messageHandler + "\" as packet ID " + packetId);
		packetId++;
	}

	@SuppressWarnings("unchecked")
	private void registerMessageClient(Class messageHandler)
	{
		network.registerMessage(messageHandler, messageHandler, packetId, Side.CLIENT);
		Lumberjack.debug("Registered client packet \"" + messageHandler + "\" as packet ID " + packetId);
		packetId++;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		instance = this;

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		eventHandler = new EventHandler();
		FMLCommonHandler.instance().bus().register(eventHandler);
		MinecraftForge.EVENT_BUS.register(eventHandler);

		tab = new PTab();

		ItemRegister.register();
		BlockRegister.register();
		CraftingRegistry.register();
		WorldRegister.register();
		EntityRegister.register();

		proxy.init();
	}

	@Mod.EventHandler
	public void init(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		//		event.registerServerCommand(new CommandChangeDimensions());
		//		event.registerServerCommand(new CommandSpawnShip());
	}
}
