package org.halvors.quantum.lib.render.block;

import com.google.common.collect.Maps;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidRegistry;
import org.halvors.quantum.client.render.RenderElectricTurbine;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.item.ISimpleItemRenderer;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.halvors.quantum.lib.tile.BlockDummy;
import org.halvors.quantum.lib.tile.TileBlock;
import org.lwjgl.opengl.GL11;

public class BlockRenderingHandler implements ISimpleBlockRenderingHandler {
    public static final BlockRenderingHandler INSTANCE = new BlockRenderingHandler();
    public static final int ID = RenderingRegistry.getNextAvailableRenderId();
    public static final Map<Block, TileEntity> inventoryTileEntities = Maps.newIdentityHashMap();

    public TileEntity getTileEntityForBlock(Block block) {
        TileEntity te = inventoryTileEntities.get(block);

        if (te == null) {
            te = block.createTileEntity(Minecraft.getMinecraft().thePlayer.getEntityWorld(), 0);
            inventoryTileEntities.put(block, te);
        }

        return te;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if ((block instanceof BlockDummy)) {
            TileBlock tile = ((BlockDummy) block).dummyTile;

            if (tile != null) {
                boolean didRender = true;
                GL11.glEnable(32826);
                GL11.glPushAttrib(262144);
                GL11.glPushMatrix();
                GL11.glTranslated(-0.5D, -0.5D, -0.5D);

                if (tile.getRenderer() != null) {
                    if (!tile.getRenderer().renderItem(new ItemStack(block, 1, metadata))) {
                        if (!tile.getRenderer().renderDynamic(new Vector3(), true, 0.0F)) {
                            if (!tile.customItemRender) {
                                GL11.glTranslated(0.5D, 0.5D, 0.5D);
                                RenderUtility.renderNormalBlockAsItem(block, metadata, renderer);
                            } else {
                                didRender = false;
                            }
                        }
                    }
                } else if (!tile.customItemRender) {
                    GL11.glTranslated(0.5D, 0.5D, 0.5D);
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
        }

        TileEntity renderTile = null;

        if (block.hasTileEntity(metadata)) {
            renderTile = getTileEntityForBlock(block);
        }

        GL11.glEnable(32826);

        if (renderTile != null) {
            GL11.glPushAttrib(262144);
            GL11.glPushMatrix();
            GL11.glTranslated(-0.5D, -0.5D, -0.5D);


            //TileEntitySpecialRenderer tesr = Minecraft.getMinecraft().re TileEntityRenderer.instance.getSpecialRendererForEntity(renderTile);
            TileEntitySpecialRenderer tesr = new RenderElectricTurbine();

            try {
                if (tesr instanceof ISimpleItemRenderer) {
                    ((ISimpleItemRenderer) tesr).renderInventoryItem(new ItemStack(block, 1, metadata));
                } else if (tesr != null) {
                    tesr.renderTileEntityAt(renderTile, 0.0D, 0.0D, 0.0D, 0.0F);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess access, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if ((block instanceof BlockDummy)) {
            BlockDummy dummy = (BlockDummy)block;
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
        return ID;
    }
}
