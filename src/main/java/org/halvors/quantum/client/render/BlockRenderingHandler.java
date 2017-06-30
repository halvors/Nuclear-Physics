package org.halvors.quantum.client.render;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;

/*
@SideOnly(Side.CLIENT)
public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    private static final BlockRenderingHandler instance = new BlockRenderingHandler();
    private static final int id = RenderingRegistry.getNextAvailableRenderId();
    private static final Map<Block, TileEntity> inventoryTileEntities = Maps.newIdentityHashMap();

    public TileEntity getTileEntityForBlock(Block block) {
        TileEntity tile = inventoryTileEntities.get(block);

        if (tile == null) {
            tile = block.createTileEntity(Minecraft.getMinecraft().player.getEntityWorld(), 0);
            inventoryTileEntities.put(block, tile);
        }

        return tile;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        // Render custom block render.
        if (block instanceof IBlockCustomRender) {
            ISimpleBlockRenderer blockRenderer = ((IBlockCustomRender) block).getRenderer();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
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
            TileEntity tile = null;

            // Render special renders.
            if (block.hasTileEntity(metadata)) {
                tile = getTileEntityForBlock(block);
            }

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            if (tile != null) {
                GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
                GL11.glPushMatrix();
                GL11.glTranslated(-0.5, -0.5, -0.5);

                TileEntitySpecialRenderer tesr = TileEntityRendererDispatcher.instance.getSpecialRenderer(tile);

                try {
                    if (tesr instanceof ISimpleItemRenderer) {
                        ((ISimpleItemRenderer) tesr).renderInventoryItem(new ItemStack(block, 1, metadata));
                    } else {
                        tesr.renderTileEntityAt(tile, 0, 0, 0, 0, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GL11.glPopMatrix();
                GL11.glPopAttrib();
            }
        }

        /*
        if (block instanceof BlockDummy) {
            TileBlock tileBlock = ((BlockDummy) block).dummyTile;

            if (tileBlock != null) {
                boolean didRender = true;
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
                GL11.glPushMatrix();
                GL11.glTranslated(-0.5, -0.5, -0.5);

                if (tileBlock.getRenderer() != null) {
                    if (!tileBlock.getRenderer().renderItem(new ItemStack(block, 1, metadata))) {
                        if (!tileBlock.getRenderer().renderDynamic(new Vector3(), true, 0)) {
                            if (!tileBlock.customItemRender) {
                                GL11.glTranslated(0.5, 0.5, 0.5);
                                RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                            } else {
                                didRender = false;
                            }
                        }
                    }
                } else if (!tileBlock.customItemRender) {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                } else {
                    didRender = false;
                }

                GL11.glPopMatrix();
                GL11.glPopAttrib();

                if (didRender) {
                    return;
                }
            }
        */
    /*
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        // Render custom block render.
        if (block instanceof IBlockCustomRender) {
            ISimpleBlockRenderer blockRenderer = ((IBlockCustomRender) block).getRenderer();

            if (blockRenderer != null) {
                if (!blockRenderer.renderStatic(renderer, access, x, y, z)) {
                    renderer.renderStandardBlock(block, x, y, z);
                }
            } else if (block.renderAsNormalBlock()) {
                renderer.renderStandardBlock(block, x, y, z);
            }

            return true;
        }

        /*
        if (block instanceof BlockDummy) {
            BlockDummy dummy = (BlockDummy) block;
            dummy.inject(access, x, y, z);
            TileBlock tileBlock = dummy.getTile(access, x, y, z);

            if (tileBlock != null) {
                tileBlock.access = access;

                if (tileBlock.getRenderer() != null) {
                    if (!tileBlock.getRenderer().renderStatic(renderer, new Vector3(x, y, z))) {
                        renderer.renderStandardBlock(block, x, y, z);
                    }
                } else if (tileBlock.normalRender || tileBlock.forceStandardRender) {
                    renderer.renderStandardBlock(block, x, y, z);
                }
            }

            return true;
        }
        */
        /*
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
*/
