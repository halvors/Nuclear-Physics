package org.halvors.nuclearphysics.client.render.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer {
    private static final IModelCustom MODEL_SMALL = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "electric_turbine_small.obj"));
    private static final IModelCustom MODEL_LARGE = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "electric_turbine_large.obj"));
    private static final ResourceLocation TEXTURE_SMALL = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "electric_turbine_small.png");
    private static final ResourceLocation TEXTURE_LARGE = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "electric_turbine_large.png");

    private static final String[] MODEL_LARGE_BLADES_SMALL = { "Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6" };
    private static final String[] MODEL_LARGE_BLADES_LARGE = { "MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6" };
    private static final String[] MODEL_LARGE_BLADES_MEDIUM = { "LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6" };
    private static final String[] MODEL_SMALL_BLADES = { "Blade1", "Blade2", "Blade3" };
    private static final String[] MODEL_SMALL_SHIELDS = { "Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6" };
    private static final String[] MODEL_SMALL_BLADES_MEDIUM = { "MediumBlade1", "MediumBlade2", "MediumBlade3" };
    private static final String[] MODEL_SMALL_SHIELDS_MEDIUM = { "MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6" };

    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        if (tile instanceof TileElectricTurbine) {
            final TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (tileTurbine.getMultiBlock().isPrimary()) {
                GL11.glPushMatrix();

                // Translate to the location of our tile entity
                GL11.glTranslated(x, y, z);
                //GL11.glDisable(GL12.GL_RESCALE_NORMAL);

                if (tileTurbine.getMultiBlock().isConstructed()) {

                    bindTexture(TEXTURE_LARGE);

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    MODEL_LARGE.renderOnly(MODEL_LARGE_BLADES_SMALL);
                    MODEL_LARGE.renderOnly(MODEL_LARGE_BLADES_LARGE);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) -Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    MODEL_LARGE.renderOnly(MODEL_LARGE_BLADES_MEDIUM);
                    GL11.glPopMatrix();

                    MODEL_LARGE.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(MODEL_LARGE_BLADES_SMALL, MODEL_LARGE_BLADES_LARGE), MODEL_LARGE_BLADES_MEDIUM));
                } else {
                    bindTexture(TEXTURE_SMALL);

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    MODEL_SMALL.renderOnly(MODEL_SMALL_BLADES);
                    MODEL_SMALL.renderOnly(MODEL_SMALL_SHIELDS);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) -Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    MODEL_SMALL.renderOnly(MODEL_SMALL_BLADES_MEDIUM);
                    MODEL_SMALL.renderOnly(MODEL_SMALL_SHIELDS_MEDIUM);
                    GL11.glPopMatrix();

                    MODEL_SMALL.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(MODEL_SMALL_BLADES, MODEL_SMALL_SHIELDS), MODEL_SMALL_BLADES_MEDIUM), MODEL_SMALL_SHIELDS_MEDIUM));
                }

                GL11.glPopMatrix();
            }
        }
    }
}
