package org.halvors.nuclearphysics.client.render.block.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer<TileElectricTurbine> {
    private static final OBJModelContainer modelLargeBladesSmall = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_large.obj"), Arrays.asList("Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6"));
    private static final OBJModelContainer modelLargeBladesLarge = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_large.obj"), Arrays.asList("MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6"));
    private static final OBJModelContainer modelLargeBladesMedium = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_large.obj"), Arrays.asList("LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6"));
    private static final OBJModelContainer modelLarge = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_large.obj"), Arrays.asList("Shape1", "Shape2", "Shape3", "Shape4", "Shape5", "Shape6", "Shape7", "Shape8", "Shape9", "Shape10", "Shape11", "Shape12", "Shape13",  "Shape14", "Shape15", "Shape16", "Shape17"));
    private static final OBJModelContainer modelSmallBlades = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_small.obj"), Arrays.asList("Blade1", "Blade2", "Blade3"));
    private static final OBJModelContainer modelSmallShields = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_small.obj"), Arrays.asList("Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6"));
    private static final OBJModelContainer modelSmallBladesMedium = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_small.obj"), Arrays.asList("MediumBlade1", "MediumBlade2", "MediumBlade3"));
    private static final OBJModelContainer modelSmallShieldsMedium = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_small.obj"), Arrays.asList("MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6"));
    private static final OBJModelContainer modelSmall = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "electric_turbine_small.obj"), Arrays.asList("Axis", "Head", "Plug", "Support"));

    @Override
    public void render(TileElectricTurbine tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (tile.getMultiBlock().isPrimary()) {
            if (tile.getMultiBlock().isConstructed()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
                GlStateManager.translate(-0.5, 0, -0.5);
                modelLargeBladesSmall.render();
                modelLargeBladesLarge.render();
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.rotate((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
                GlStateManager.translate(-0.5, 0, -0.5);
                modelLargeBladesMedium.render();
                GlStateManager.popMatrix();

                modelLarge.render();
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
                GlStateManager.translate(-0.5, 0, -0.5);
                modelSmallBlades.render();
                modelSmallShields.render();
                GlStateManager.popMatrix();

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.rotate((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
                GlStateManager.translate(-0.5, 0, -0.5);
                modelSmallBladesMedium.render();
                modelSmallShieldsMedium.render();
                GlStateManager.popMatrix();

                modelSmall.render();
            }
        }
    }
}