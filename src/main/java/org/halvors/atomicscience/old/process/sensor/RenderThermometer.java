package org.halvors.atomicscience.old.process.sensor;

import calclavia.lib.render.RenderUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.vector.Vector3;

@SideOnly(Side.CLIENT)
public class RenderThermometer
        extends TileEntitySpecialRenderer
{
    public void func_76894_a(TileEntity tileEntity, double x, double y, double z, float var8)
    {
        TileThermometer tile = (TileThermometer)tileEntity;

        GL11.glPushMatrix();
        RenderUtility.enableLightmap();
        for (int side = 2; side < 6; side++)
        {
            RenderUtility.renderText((tile.isOverThreshold() ? "�4" : "") + Math.round(tile.detectedTemperature) + " K", side, 0.8F, x, y + 0.1D, z);
            RenderUtility.renderText((tile.isOverThreshold() ? "�4" : "�1") + "Threshold: " + tile.getThershold() + " K", side, 1.0F, x, y - 0.1D, z);
            if (tile.trackCoordinate != null) {
                RenderUtility.renderText(tile.trackCoordinate.intX() + ", " + tile.trackCoordinate.intY() + ", " + tile.trackCoordinate.intZ(), side, 0.5F, x, y - 0.3D, z);
            }
        }
        GL11.glPopMatrix();
    }
}
