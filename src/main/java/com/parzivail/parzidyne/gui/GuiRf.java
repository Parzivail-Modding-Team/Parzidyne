package com.parzivail.parzidyne.gui;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.container.ContainerNothing;
import com.parzivail.parzidyne.tile.TileRF;
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

import java.util.EnumSet;

public class GuiRf extends GuiContainer
{
	private static final ResourceLocation guiTexture = Resources.location("textures/container/personalDatapad.png");
	private final EntityPlayer player;
	private final TileRF tile;
	private int attachmentIdx;

	public GuiRf(InventoryPlayer inventoryPlayer, TileRF tile)
	{
		super(new ContainerNothing());

		// We have to use this awful hack because the EntityPlayer that's provided to
		// the Gui through the InventoryPlayer is a strictly client-based player instance
		// and isn't the real one.
		player = (EntityPlayer)DimensionManager.getWorld(inventoryPlayer.player.dimension).getEntityByID(inventoryPlayer.player.getEntityId());
		this.tile = tile;
	}

	@Override
	public void initGui()
	{
		xSize = 256;
		ySize = 241;

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
		String title = I18n.format(Resources.guiDot("datapad.logotype"));
		fontRendererObj.drawString(title, (xSize - fontRendererObj.getStringWidth(title)) / 2, 139, 0x0D0D0D);

		fontRendererObj.drawString(String.format("%d RF", tile.getEnergyStored(null)), 7, 7, GLPalette.ANALOG_GREEN);

		GL.PushAttrib(EnumSet.of(AttribMask.EnableBit, AttribMask.LineBit));
		GL.Disable(EnableCap.Texture2D);
		GL11.glLineWidth(2);

		Fx.D2.DrawLine(7, 15, 249, 15);
		GL.PopAttrib();
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
