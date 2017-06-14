package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.render.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/nuclearBoiler.obj"));
    private static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/nuclearBoiler.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
        if (tile instanceof TileNuclearBoiler) {
            TileNuclearBoiler tileNuclearBoiler = (TileNuclearBoiler) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);
            GL11.glRotated(90, 0, 1, 0);

            if (tileNuclearBoiler.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileNuclearBoiler.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glTranslated(-0.187042F, 0, 0.312958F);
            GL11.glRotated(Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0);
            GL11.glTranslated(0.187042F, 0, -0.312958F);
            model.renderOnly("FuelBarSupport1Rotates", "FuelBar1Rotates");
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0.187042F, 0, 0.312958F);
            GL11.glRotated(-Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0);
            GL11.glTranslated(-0.187042F, 0, -0.312958F);
            model.renderOnly("FuelBarSupport2Rotates", "FuelBar2Rotates");
            GL11.glPopMatrix();

            model.renderAllExcept("FuelBarSupport1Rotates", "FuelBarSupport2Rotates", "FuelBar1Rotates", "FuelBar2Rotates");
            GL11.glPopMatrix();
        }
    }
}