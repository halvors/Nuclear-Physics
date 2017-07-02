package org.halvors.quantum.client.render.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.quantum.client.render.OBJBakedModel;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer<TileElectricTurbine> {
    private static final OBJBakedModel modelLargeBladesSmall = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_large.obj"), Arrays.asList("Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6"));
    private static final OBJBakedModel modelLargeBladesLarge = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_large.obj"), Arrays.asList("MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6"));
    private static final OBJBakedModel modelLargeBladesMedium = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_large.obj"), Arrays.asList("LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6"));
    private static final OBJBakedModel modelLargeAll = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_large.obj"), Arrays.asList("Shape1", "Shape2", "Shape3", "Shape4", "Shape5", "Shape6", "Shape7", "Shape8", "Shape9", "Shape10", "Shape11", "Shape12", "Shape13", "Shape14", "Shape15", "Shape16", "Shape17"));

    private static final OBJBakedModel modelSmallBlades = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_small.obj"), Arrays.asList("Blade1", "Blade2", "Blade3"));
    private static final OBJBakedModel modelSmallShields = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_small.obj"), Arrays.asList("Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6"));
    private static final OBJBakedModel modelSmallBladesMedium = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_small.obj"), Arrays.asList("MediumBlade1", "MediumBlade2", "MediumBlade3"));
    private static final OBJBakedModel modelSmallShieldsMedium = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_small.obj"), Arrays.asList("MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6"));
    private static final OBJBakedModel modelSmallAll = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "electric_turbine_small.obj"), Arrays.asList("Axis", "Head", "Plug", "Support"));

    private static final ResourceLocation textureSmall = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electricTurbineSmall.png");
    private static final ResourceLocation textureLarge = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electricTurbineLarge.png");

    @Override
    public void renderTileEntityAt(TileElectricTurbine tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile.getMultiBlock().isPrimary()) {
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();

            // Translate to the location of our tile entity
            GlStateManager.translate(x + 0.5, y, z + 0.5);
            GlStateManager.disableRescaleNormal();

            if (tile.getMultiBlock().isConstructed()) {
                bindTexture(textureLarge);

                GlStateManager.pushMatrix();
                GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
                modelLargeBladesSmall.render();
                modelLargeBladesLarge.render();
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.rotate((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
                modelLargeBladesMedium.render();
                GlStateManager.popMatrix();

                modelLargeAll.render();
            } else {
                GlStateManager.scale(1, 1.1, 1);
                bindTexture(textureSmall);

                GlStateManager.pushMatrix();
                GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
                modelSmallBlades.render();
                modelSmallShields.render();
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.rotate((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
                modelSmallBladesMedium.render();
                modelSmallShieldsMedium.render();
                GlStateManager.popMatrix();

                modelSmallAll.render();
            }

            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }
    }
}