package org.halvors.quantum.client.render.machine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractorMcJty extends TileEntitySpecialRenderer<TileChemicalExtractor> {
    private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
                model = ModelLoaderRegistry.getModel(new ResourceLocation(Reference.ID, "block/chemical_extractor.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }

        return bakedModel;
    }

    @Override
    public void renderTileEntityAt(TileChemicalExtractor tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        renderHandles(tile);

        // Render our item
        renderItem(tile);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private void renderHandles(TileChemicalExtractor tile) {
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0, .5);
        //long angle = (System.currentTimeMillis() / 10) % 360;
        //GlStateManager.rotate(angle, 0, 1, 0);

        RenderHelper.disableStandardItemLighting();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = tile.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
        GlStateManager.translate(-tile.getPos().getX(), -tile.getPos().getY(), -tile.getPos().getZ());

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                world,
                getBakedModel(),
                world.getBlockState(tile.getPos()),
                tile.getPos(),
                tessellator.getBuffer(),
                false);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void renderItem(TileChemicalExtractor tile) {
        /*
        ItemStack stack = tile.getStack();

        if (stack != null) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(.5, .9, .5);
            GlStateManager.scale(.4f, .4f, .4f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
        */
    }
}