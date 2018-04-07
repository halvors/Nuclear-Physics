package org.halvors.nuclearphysics.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.client.render.item.ISimpleItemRenderer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    private static final BlockRenderingHandler instance = new BlockRenderingHandler();
    private static final int id = RenderingRegistry.getNextAvailableRenderId();
    private static final Map<ItemStack, TileEntity> tileEntitiesCache = new HashMap<>();

    public TileEntity getTileEntityForBlock(Block block, int metadata) {
        ItemStack itemStack = new ItemStack(block, 1, metadata);
        TileEntity tile = tileEntitiesCache.get(itemStack);

        if (tile == null) {
            tile = block.createTileEntity(Minecraft.getMinecraft().thePlayer.getEntityWorld(), metadata);
            tileEntitiesCache.put(itemStack, tile);
        }

        return tile;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        // Render custom block render.
        if (block instanceof ICustomBlockRenderer) {
            ISimpleBlockRenderer blockRenderer = ((ICustomBlockRenderer) block).getRenderer();

            GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();

            if (blockRenderer != null) {
                if (!blockRenderer.renderItem(new ItemStack(block, 1, metadata))) {
                    if (block.renderAsNormalBlock()) {
                        RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                    }
                }
            }

            GL11.glPopMatrix();
            GL11.glPopAttrib();
        } else {
            if (block.hasTileEntity(metadata)) {
                final TileEntity tile = getTileEntityForBlock(block, metadata);

                if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(tile)) {
                    GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
                    GL11.glPushMatrix();
                    GL11.glTranslated(-0.5, -1.25, -0.5);
                    GL11.glRotated(180, 0, 1, 0);

                    TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);

                    try {
                        if (tesr instanceof ISimpleItemRenderer) {
                            ((ISimpleItemRenderer) tesr).renderInventoryItem(new ItemStack(block, 1, metadata));
                        } else {
                            tesr.renderTileEntityAt(tile, 0, 0, 0, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    GL11.glPopMatrix();
                    GL11.glPopAttrib();
                } else {
                    if (block.renderAsNormalBlock()) {
                        RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                    }
                }
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // Render custom block render.
        if (block instanceof ICustomBlockRenderer) {
            final ISimpleBlockRenderer blockRenderer = ((ICustomBlockRenderer) block).getRenderer();

            if (blockRenderer != null) {
                if (!blockRenderer.renderStatic(renderer, world, x, y, z)) {
                    renderer.renderStandardBlock(block, x, y, z);

                    return true;
                }
            } else if (block.renderAsNormalBlock()) {
                renderer.renderStandardBlock(block, x, y, z);

                return true;
            }
        } else {
            final TileEntity tile = world.getTileEntity(x, y, z);

            if (block.renderAsNormalBlock() && !TileEntityRendererDispatcher.instance.hasSpecialRenderer(tile)) {
                renderer.renderStandardBlock(block, x, y, z);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return id;
    }

    public static BlockRenderingHandler getInstance() {
        return instance;
    }
}
