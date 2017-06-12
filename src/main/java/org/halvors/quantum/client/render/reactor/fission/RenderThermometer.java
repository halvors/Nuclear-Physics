package org.halvors.quantum.client.render.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.utility.render.Color;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderThermometer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileThermometer tile = (TileThermometer) tileEntity;

        GL11.glPushMatrix();
        RenderUtility.enableLightmap();

        for (int side = 2; side < 6; side++) {
            RenderUtility.renderText((tile.isOverThreshold() ? Color.DARK_RED : Color.BLACK) + Integer.toString(Math.round(tile.detectedTemperature)) + " K", side, 0.8f, x, y + 0.1, z);
            RenderUtility.renderText((tile.isOverThreshold() ? Color.DARK_RED : Color.DARK_BLUE) + "Threshold: " + (tile.getThershold()) + " K", side, 1, x, y - 0.1, z);

            if (tile.trackCoordinate != null) {
                RenderUtility.renderText(tile.trackCoordinate.intX() + ", " + tile.trackCoordinate.intY() + ", " + tile.trackCoordinate.intZ(), side, 0.5f, x, y - 0.3, z);
            }
        }

        GL11.glPopMatrix();
    }
}
