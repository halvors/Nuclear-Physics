package org.halvors.quantum.lib.render;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.particle.BlockFulmination;
import org.halvors.quantum.common.block.reactor.BlockGasFunnel;
import org.halvors.quantum.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.item.ISimpleItemRenderer;
import org.halvors.quantum.lib.tile.BlockDummy;
import org.halvors.quantum.lib.tile.TileBlock;
import org.halvors.quantum.lib.utility.WorldUtility;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    private static final BlockRenderingHandler instance = new BlockRenderingHandler();
    private static final int id = RenderingRegistry.getNextAvailableRenderId();
    private static final Map<Block, TileEntity> inventoryTileEntities = Maps.newIdentityHashMap();

    public TileEntity getTileEntityForBlock(Block block) {
        TileEntity tileEntity = inventoryTileEntities.get(block);

        if (tileEntity == null) {
            tileEntity = block.createTileEntity(Minecraft.getMinecraft().thePlayer.getEntityWorld(), 0);
            inventoryTileEntities.put(block, tileEntity);
        }

        return tileEntity;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        TileEntity tile = null;

        // Render electromagnet.
        if (block instanceof BlockElectromagnet || block instanceof BlockFulmination) {
            IIcon edgeTexture = RenderUtility.getIcon(Reference.PREFIX + "atomic_edge");

            GL11.glPushMatrix();
            BlockRenderUtility.tessellateBlockWithConnectedTextures(metadata, block, null, edgeTexture);
            GL11.glPopMatrix();

            return;
        } else if (block instanceof BlockGasFunnel) {
            IIcon edgeTexture = RenderUtility.getIcon(Reference.PREFIX + "gasFunnel_edge");

            GL11.glPushMatrix();
            BlockRenderUtility.tessellateBlockWithConnectedTextures(metadata, block, null, edgeTexture);
            GL11.glPopMatrix();

            return;
        } else if (block instanceof BlockDummy) { // TODO: Is this needed anymore?
            TileBlock tileBlock = ((BlockDummy) block).dummyTile;

            if (tile != null) {
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
        } else {
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
                        tesr.renderTileEntityAt(tile, 0, 0, 0, 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                GL11.glPopMatrix();
                GL11.glPopAttrib();
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileEntity tile = access.getTileEntity(x, y, z);

        // Render electromagnet.
        if (block instanceof BlockElectromagnet || block instanceof BlockFulmination) {
            IIcon edgeTexture = RenderUtility.getIcon(Reference.PREFIX + "atomic_edge");
            byte sideMap = 0;

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                Vector3 check = new Vector3(x, y, z).translate(direction);
                TileEntity checkTile = check.getTileEntity(access);

                if (checkTile != null && checkTile.getClass() == tile.getClass() && check.getBlockMetadata(access) == tile.getBlockMetadata()) {
                    sideMap = WorldUtility.setEnableSide(sideMap, direction, true);
                }
            }

            BlockRenderUtility.tessellateBlockWithConnectedTextures(sideMap, access, x, y, z, block, null, edgeTexture);

            return true;
        // Render gas funnel.
        } else if (block instanceof BlockGasFunnel) {
            IIcon edgeTexture = RenderUtility.getIcon(Reference.PREFIX + "gasFunnel_edge");
            byte sideMap = 0;

            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                Vector3 check = new Vector3(x, y, z).translate(direction);
                TileEntity checkTile = check.getTileEntity(access);

                if (checkTile != null && checkTile.getClass() == tile.getClass() && check.getBlockMetadata(access) == tile.getBlockMetadata()) {
                    sideMap = WorldUtility.setEnableSide(sideMap, direction, true);
                }
            }

            BlockRenderUtility.tessellateBlockWithConnectedTextures(sideMap, access, x, y, z, block, null, edgeTexture);

        } else if (block instanceof BlockDummy) { // TODO: Is this really needed anymore?
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

    public static int getId() {
        return id;
    }
}
