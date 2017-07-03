package org.halvors.quantum.client.render.machine;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.render.ModelHandle;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer<TileChemicalExtractor> {
    //private static final OBJBakedModel modelRotate = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates"));
    //private static final OBJBakedModel modelAll = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("CornerSupport1", "CornerSupport2", "CornerSupport3", "CornerSupport4", "EnergyPlug", "Keyboard", "KeyboardSupport", "MainBase", "SupportBeam1", "SupportBeam2"));

    private static final ModelHandle modelAll = ModelHandle.of(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"));

    @Override
    public void renderTileEntityAt(TileChemicalExtractor tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        /*
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        */

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        //GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        //GlStateManager.disableRescaleNormal();

        /*
        if (tile.getWorld() != null) {
            RenderUtility.rotateBlockBasedOnDirection(tile.getDirection());
        }
        */

        /*
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.1875, 0.4375, 0);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 0, 1);
        GlStateManager.translate(-0.1875, -0.4375, 0);
        //modelRotate.render();
        GlStateManager.popMatrix();
        */

        modelAll.render();

        GlStateManager.popMatrix();
    }
}