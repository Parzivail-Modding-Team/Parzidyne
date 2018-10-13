package com.parzivail.parzidyne.gui;

import com.parzivail.parzidyne.Resources;
import com.parzivail.parzidyne.container.ContainerNothing;
import com.parzivail.parzidyne.gui.modules.PGuiButton;
import com.parzivail.parzidyne.network.TransactionBroker;
import com.parzivail.parzidyne.network.transaction.TransactionToggleQuarry;
import com.parzivail.parzidyne.tile.TileQuarry;
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

public class GuiQuarry extends GuiContainer
{
	private static final ResourceLocation guiTexture = Resources.location("textures/container/quarry.png");
	private final EntityPlayer player;
	private final TileQuarry tile;

	private PGuiButton bStartStop;

	public GuiQuarry(InventoryPlayer inventoryPlayer, TileQuarry tile)
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
		xSize = 176;
		ySize = 179;

		super.initGui();
		buttonList.clear();

		buttonList.add(bStartStop = new PGuiButton(0, guiLeft + xSize - 46, guiTop + 16, 40, 15, "Toggle"));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button == bStartStop)
		{
			TransactionBroker.dispatch(new TransactionToggleQuarry(tile, !tile.isMining()));
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String machine = I18n.format(Resources.guiDot("quarry.name"));
		this.fontRendererObj.drawString(machine, this.xSize / 2 - this.fontRendererObj.getStringWidth(machine) / 2, 6, GLPalette.ALMOST_BLACK);

		String inventory = I18n.format("container.inventory");
		fontRendererObj.drawString(inventory, 7, 87, 0x0D0D0D);

		String runningStr = tile.isMining() ? I18n.format(Resources.guiDot("quarry.running")) : I18n.format(Resources.guiDot("quarry.stopped"));
		fontRendererObj.drawString(runningStr, 26, 20, tile.isMining() ? GLPalette.ANALOG_GREEN : GLPalette.ANALOG_RED);

		fontRendererObj.drawString(String.format("Layer: %d", tile.getLayer()), 26, 20 + fontRendererObj.FONT_HEIGHT, GLPalette.ALMOST_BLACK);

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
