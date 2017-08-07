package org.halvors.quantum.atomic.client.render.block.reactor.fission;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.utility.RenderUtility;
import org.halvors.quantum.atomic.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.atomic.common.utility.type.Color;

@SideOnly(Side.CLIENT)
public class RenderThermometer extends TileEntitySpecialRenderer<TileThermometer> {
    @Override
    public void renderTileEntityAt(TileThermometer tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        RenderUtility.enableLightmap();

        // TODO: Replacement for this...
        for (EnumFacing side : EnumFacing.HORIZONTALS) {
            RenderUtility.renderText((tile.isOverThreshold() ? Color.DARK_RED : Color.BLACK) + Integer.toString(Math.round(tile.getDetectedTemperature())) + " K", side, 0.8F, x, y + 0.1, z);
            RenderUtility.renderText((tile.isOverThreshold() ? Color.DARK_RED : Color.DARK_BLUE) + "Threshold: " + (tile.getThershold()) + " K", side, 1, x, y - 0.1, z);

            if (tile.getTrackCoordinate() != null) {
                RenderUtility.renderText(tile.getTrackCoordinate().intX() + ", " + tile.getTrackCoordinate().intY() + ", " + tile.getTrackCoordinate().intZ(), side, 0.5F, x, y - 0.3, z);
            }
        }

        GlStateManager.popMatrix();
    }
}
