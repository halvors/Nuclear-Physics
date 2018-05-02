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
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer {
    private static final IModelCustom modelSmall = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "electric_turbine_small.obj"));
    private static final IModelCustom modelLarge = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "electric_turbine_large.obj"));
    private static final ResourceLocation textureSmall = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "electric_turbine_small.png");
    private static final ResourceLocation textureLarge = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "electric_turbine_large.png");

    private static final String[] modelLargeBladesSmall = { "Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6" };
    private static final String[] modelLargeBladesLarge = { "MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6" };
    private static final String[] modelLargeBladesMedium = { "LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6" };
    private static final String[] modelSmallBlades = { "Blade1", "Blade2", "Blade3" };
    private static final String[] modelSmallShields = { "Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6" };
    private static final String[] modelSmallBladesMedium = { "MediumBlade1", "MediumBlade2", "MediumBlade3" };
    private static final String[] modelSmallShieldsMedium = { "MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6" };

    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        if (tile instanceof TileElectricTurbine) {
            final TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (tileTurbine.getMultiBlock().isPrimary()) {
                GL11.glPushMatrix();

                // Translate to the location of our tile entity
                GL11.glTranslated(x, y, z);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);

                if (tileTurbine.getMultiBlock().isConstructed()) {
                    bindTexture(textureLarge);

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    modelLarge.renderOnly(modelLargeBladesSmall);
                    modelLarge.renderOnly(modelLargeBladesLarge);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) -Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    modelLarge.renderOnly(modelLargeBladesMedium);
                    GL11.glPopMatrix();

                    modelLarge.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(modelLargeBladesSmall, modelLargeBladesLarge), modelLargeBladesMedium));
                } else {
                    bindTexture(textureSmall);

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    modelSmall.renderOnly(modelSmallBlades);
                    modelSmall.renderOnly(modelSmallShields);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated((float) -Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                    modelSmall.renderOnly(modelSmallBladesMedium);
                    modelSmall.renderOnly(modelSmallShieldsMedium);
                    GL11.glPopMatrix();

                    modelSmall.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(modelSmallBlades, modelSmallShields), modelSmallBladesMedium), modelSmallShieldsMedium));
                }

                GL11.glPopMatrix();
            }
        }
    }
}