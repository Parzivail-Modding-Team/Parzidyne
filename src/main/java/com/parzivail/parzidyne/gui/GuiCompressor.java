package com.parzivail.parzidyne.gui;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.tile.TileCompressor;
import com.parzivail.util.math.MathUtil;
import com.parzivail.util.ui.Fx;
import com.parzivail.util.ui.GLPalette;
import com.parzivail.util.ui.gltk.AttribMask;
import com.parzivail.util.ui.gltk.EnableCap;
import com.parzivail.util.ui.gltk.GL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GuiCompressor extends GuiContainer
{
	private static final ResourceLocation guiTexture = Resources.location("textures/container/compressor.png");
	private final EntityPlayer player;
	private final TileCompressor tile;

	public GuiCompressor(InventoryPlayer inventoryPlayer, TileCompressor tile)
	{
		super(new ContainerCompressor(inventoryPlayer, tile));

		// We have to use this awful hack because the EntityPlayer that's provided to
		// the Gui through the InventoryPlayer is a strictly client-based player instance
		// and isn't the real one.
		player = (EntityPlayer)DimensionManager.getWorld(inventoryPlayer.player.dimension).getEntityByID(inventoryPlayer.player.getEntityId());
		this.tile = tile;
	}

	@Override
	public void initGui()
	{
		xSize = 176;
		ySize = 179;

		super.initGui();
		buttonList.clear();
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String machine = I18n.format(tile.getInventoryName());
		this.fontRendererObj.drawString(machine, this.xSize / 2 - this.fontRendererObj.getStringWidth(machine) / 2, 6, GLPalette.ALMOST_BLACK);

		String inventory = I18n.format("container.inventory");
		fontRendererObj.drawString(inventory, 7, 87, 0x0D0D0D);

		this.mc.getTextureManager().bindTexture(guiTexture);
		GL.Color(GLPalette.WHITE);

		if (this.tile.progress > 0)
		{
			int scaledProgress = (int)(this.tile.progress / 200f * 41);
			this.drawTexturedModalRect(67, 33, 176, 0, scaledProgress + 1, 28);
		}

		String rfString = String.format("%d RF", tile.getEnergyStored(null));

		GL.PushAttrib(AttribMask.EnableBit);
		GL.Disable(EnableCap.Texture2D);

		float powerScaled = (tile.energyStorage.getEnergyStored() / (float)tile.energyStorage.getMaxEnergyStored()) * 64;
		GL.Color(GLPalette.ANALOG_RED);
		Fx.D2.DrawSolidRectangle(6, 16 + (64 - powerScaled), 16, powerScaled);

		if (MathUtil.rectContains(guiLeft + 6, guiTop + 16, 16, 64, mouseX, mouseY))
		{
			ArrayList<String> l = new ArrayList<>();
			l.add(rfString);
			drawHoveringText(l, mouseX - guiLeft, mouseY - guiTop);
		}

		GL.PopAttrib();

		GL.Color(GLPalette.WHITE);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTexture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
