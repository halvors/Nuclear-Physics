package org.halvors.quantum.client.render.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.client.utility.render.Color;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderThermometer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            GL11.glPushMatrix();
            RenderUtility.enableLightmap();

            for (int side = 2; side < 6; side++) {
                RenderUtility.renderText((tileThermometer.isOverThreshold() ? Color.DARK_RED : Color.BLACK) + Integer.toString(Math.round(tileThermometer.detectedTemperature)) + " K", side, 0.8F, x, y + 0.1, z);
                RenderUtility.renderText((tileThermometer.isOverThreshold() ? Color.DARK_RED : Color.DARK_BLUE) + "Threshold: " + (tileThermometer.getThershold()) + " K", side, 1, x, y - 0.1, z);

                if (tileThermometer.trackCoordinate != null) {
                    RenderUtility.renderText(tileThermometer.trackCoordinate.intX() + ", " + tileThermometer.trackCoordinate.intY() + ", " + tileThermometer.trackCoordinate.intZ(), side, 0.5F, x, y - 0.3, z);
                }
            }

            GL11.glPopMatrix();
        }
    }
}
