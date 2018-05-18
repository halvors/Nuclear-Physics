package org.halvors.nuclearphysics.client.render.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderThermometer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            GL11.glPushMatrix();
            RenderUtility.enableLightmap();

            RenderUtility.renderText((tileThermometer.isOverThreshold() ? EnumColor.DARK_RED : EnumColor.BLACK) + UnitDisplay.getTemperatureDisplay(Math.round(tileThermometer.getDetectedTemperature())), tileThermometer.getFacing(), 0.8F, x, y + 0.1, z);
            RenderUtility.renderText((tileThermometer.isOverThreshold() ? EnumColor.DARK_RED : EnumColor.DARK_BLUE) + "Threshold: " + UnitDisplay.getTemperatureDisplay(tileThermometer.getThershold()), tileThermometer.getFacing(), 1, x, y - 0.1, z);

            final BlockPos trackCoordinate = tileThermometer.getTrackCoordinate();

            if (tileThermometer.getTrackCoordinate() != null) {
                RenderUtility.renderText(trackCoordinate.getX() + ", " + trackCoordinate.getY() + ", " + trackCoordinate.getZ(), tileThermometer.getFacing(), 0.5F, x, y - 0.3, z);
            }

            GL11.glPopMatrix();
        }
    }
}
