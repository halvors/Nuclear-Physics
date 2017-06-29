package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "nuclearBoiler.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "nuclearBoiler.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        if (tile instanceof TileNuclearBoiler) {
            TileNuclearBoiler tileNuclearBoiler = (TileNuclearBoiler) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            if (tileNuclearBoiler.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileNuclearBoiler.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glTranslated(0.312958, 0, 0.187042);
            GL11.glRotated(Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0);
            GL11.glTranslated(-0.312958, 0, -0.187042);
            model.renderOnly("FuelBarSupport1Rotates", "FuelBar1Rotates");
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0.312958, 0, -0.187042);
            GL11.glRotated(-Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0);
            GL11.glTranslated(-0.312958, 0, 0.187042);
            model.renderOnly("FuelBarSupport2Rotates", "FuelBar2Rotates");
            GL11.glPopMatrix();

            model.renderAllExcept("FuelBarSupport1Rotates", "FuelBarSupport2Rotates", "FuelBar1Rotates", "FuelBar2Rotates");
            GL11.glPopMatrix();
        }
    }
}