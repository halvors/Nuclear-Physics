package org.halvors.quantum.client.render.machine;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractorTest extends TileEntitySpecialRenderer<TileChemicalExtractor> {
    //private static final OBJBakedModel modelRotate = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates"));
    //private static final OBJBakedModel modelAll = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"), Arrays.asList("CornerSupport1", "CornerSupport2", "CornerSupport3", "CornerSupport4", "EnergyPlug", "Keyboard", "KeyboardSupport", "MainBase", "SupportBeam1", "SupportBeam2"));

    private static final ResourceLocation texture = new ResourceLocation(Reference.ID, "textures/models/chemical_extractor.png");
    private static OBJModel.OBJBakedModel model;

    public static void drawBakedModel(IBakedModel model) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        for (BakedQuad bakedquad : model.getQuads(null, null, 0))
        {
            worldrenderer.addVertexData(bakedquad.getVertexData());
        }

        tessellator.draw();
    }

    private void updateModels() {
        if (model == null) {
            try {
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                OBJModel objModel = (OBJModel) ModelLoaderRegistry.getModel(ResourceUtility.getResource(ResourceType.MODEL, "chemical_extractor.obj"));
                objModel = (OBJModel) objModel.process(ImmutableMap.of("flip-v", "true"));
                model = (OBJModel.OBJBakedModel) objModel.bake(new OBJModel.OBJState(Arrays.asList("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates", "CornerSupport1", "CornerSupport2", "CornerSupport3", "CornerSupport4", "EnergyPlug", "Keyboard", "KeyboardSupport", "MainBase", "SupportBeam1", "SupportBeam2"), true), DefaultVertexFormats.ITEM, spriteFunction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileChemicalExtractor tile, double x, double y, double z, float partialTicks, int destroyStage) {
        updateModels();

        //bindTexture(texture);

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

        drawBakedModel(model);

        GlStateManager.popMatrix();
    }
}