package com.parzivail.parzidyne.render;

import com.parzivail.parzidyne.proxy.Client;
import com.parzivail.parzidyne.tile.TileQuarry;
import com.parzivail.util.ui.Fx;
import com.parzivail.util.ui.GLPalette;
import com.parzivail.util.ui.gltk.AttribMask;
import com.parzivail.util.ui.gltk.EnableCap;
import com.parzivail.util.ui.gltk.GL;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.EnumSet;

public class RenderQuarry extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks)
	{
		if (!(tile instanceof TileQuarry))
			return;

		TileQuarry quarry = (TileQuarry)tile;
		if (!quarry.isMining() || !quarry.canMine())
			return;

		GL.PushMatrix();
		GL.PushAttrib(EnumSet.of(AttribMask.EnableBit, AttribMask.LineBit));
		GL.Disable(EnableCap.Texture2D);
		GL.Disable(EnableCap.Lighting);
		GL.Enable(EnableCap.Blend);
		Client.mc.entityRenderer.disableLightmap(0);

		Vec3 playerPos = Client.mc.thePlayer.getPosition(partialTicks);
		GL.Translate(-playerPos.xCoord, -playerPos.yCoord, -playerPos.zCoord);

		GL11.glLineWidth(10);
		GL.Color(GLPalette.ANALOG_BLUE, 128);
		Vector3f end = quarry.getTarget();

		Fx.D3.DrawSolidBoxSkew(0.125, tile.xCoord + 0.5, tile.yCoord - 0.125f, tile.zCoord + 0.5, end.x + 0.5, end.y + 1.125f, end.z + 0.5);

		GL.PushMatrix();
		GL.Color(GLPalette.ANALOG_BLUE, 96);
		GL.Translate(end.x + 0.5, end.y + 0.5, end.z + 0.5);
		Fx.D3.DrawSolidBox();
		GL.PopMatrix();

		GL.PopAttrib();
		GL.PopMatrix();
	}
}
