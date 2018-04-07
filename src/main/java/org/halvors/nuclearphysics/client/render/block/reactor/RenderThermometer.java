package org.halvors.nuclearphysics.client.render.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.unit.UnitDisplay;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderThermometer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            GL11.glPushMatrix();
            RenderUtility.enableLightmap();

            RenderUtility.renderText((tileThermometer.isOverThreshold() ? Color.DARK_RED : Color.BLACK) + UnitDisplay.getTemperatureDisplay(Math.round(tileThermometer.getDetectedTemperature())), tileThermometer.getFacing(), 0.8F, x, y + 0.1, z);
            RenderUtility.renderText((tileThermometer.isOverThreshold() ? Color.DARK_RED : Color.DARK_BLUE) + "Threshold: " + UnitDisplay.getTemperatureDisplay(tileThermometer.getThershold()), tileThermometer.getFacing(), 1, x, y - 0.1, z);

            Position trackCoordinate = tileThermometer.getTrackCoordinate();

            if (tileThermometer.getTrackCoordinate() != null) {
                RenderUtility.renderText(trackCoordinate.getIntX() + ", " + trackCoordinate.getIntY() + ", " + trackCoordinate.getIntZ(), tileThermometer.getFacing(), 0.5F, x, y - 0.3, z);
            }

            GL11.glPopMatrix();
        }
    }
}
