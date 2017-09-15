package org.halvors.nuclearphysics.client.render.block.process;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.process.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.Resource;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer<TileNuclearBoiler> {
    private static final OBJModelContainer modelPart1 = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "nuclear_boiler.obj"), Arrays.asList("FuelBarSupport1Rotates", "FuelBar1Rotates"));
    private static final OBJModelContainer modelPart2 = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "nuclear_boiler.obj"), Arrays.asList("FuelBarSupport2Rotates", "FuelBar2Rotates"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "nuclear_boiler.obj"), Arrays.asList("Base", "RadShieldPlate1", "RadShieldPlate2", "RadShieldPlate3", "Support", "ThermalDisplay", "TopSupport1", "TopSupport2", "TopSupport3"));

    @Override
    public void render(TileNuclearBoiler tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.687042, 0, 0.1875);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GlStateManager.translate(-0.687042, 0, -0.1875);
        modelPart1.render();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.312958, 0, 0.1875);
        GlStateManager.rotate((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
        GlStateManager.translate(-0.312958, 0, -0.1875);
        modelPart2.render();
        GlStateManager.popMatrix();

        model.render();

        GlStateManager.popMatrix();
    }
}