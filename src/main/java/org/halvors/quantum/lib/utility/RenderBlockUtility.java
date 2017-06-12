package org.halvors.quantum.lib.utility;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.render.RenderUtility;
import org.lwjgl.opengl.GL11;

public class RenderBlockUtility {
    public static void setupLight(World world, int x, int y, int z) {
        if (world.getBlock(x, y, z).isOpaqueCube()) {
            return;
        }

        int br = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var11 = br % 65536;
        int var12 = br / 65536;
        float scale = 1;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 * scale, var12 * scale);
    }

    public static void tessellateFace(RenderBlocks renderBlocks, Block block, IIcon overrideTexture, int side) {
        Tessellator tessellator = Tessellator.instance;
        int meta = 0;
        IIcon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(side, meta);

        if (side == 0) {
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(block, 0, 0, 0, useTexture);
        }

        if (side == 1) {
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(block, 0, 0, 0, useTexture);
        }

        if (side == 2) {
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(block, 0, 0, 0, useTexture);
        }

        if (side == 3) {
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(block, 0, 0, 0, useTexture);
        }

        if (side == 4) {
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(block, 0, 0, 0, useTexture);
        }

        if (side == 5) {
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(block, 0, 0, 0, useTexture);
        }
    }

    public static void tessellateFace(RenderBlocks renderBlocks, IBlockAccess access, int x, int y, int z, Block block, IIcon overrideTexture, int side) {
        Tessellator tessellator = Tessellator.instance;
        IIcon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(access, x, y, z, side);

        if (side == 0) {
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderBlocks.renderFaceYNeg(block, x, y, z, useTexture);
        }

        if (side == 1) {
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderBlocks.renderFaceYPos(block, x, y, z, useTexture);
        }

        if (side == 2) {
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderBlocks.renderFaceZNeg(block, x, y, z, useTexture);
        }

        if (side == 3) {
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderBlocks.renderFaceZPos(block, x, y, z, useTexture);
        }

        if (side == 4) {
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXNeg(block, x, y, z, useTexture);
        }

        if (side == 5) {
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderBlocks.renderFaceXPos(block, x, y, z, useTexture);
        }
    }

    /**
     * Renders a connected texture block with a bitmask
     * @param sideMap - The sides that are connected
     */
    public static void tessellateBlockWithConnectedTextures(byte sideMap, IBlockAccess access, int x, int y, int z, Block block, IIcon faceOverride, IIcon edgeOverride) {
        tessellateBlockWithConnectedTextures(sideMap, RenderUtility.renderBlocks, access, x, y, z, block, faceOverride, edgeOverride);
    }

    public static void tessellateBlockWithConnectedTextures(byte sideMap, RenderBlocks renderBlocks, IBlockAccess access, int x, int y, int z, Block block, IIcon faceOverride, IIcon edgeOverride) {
        renderBlocks.blockAccess = access;

        if (faceOverride != null) {
            renderBlocks.setOverrideBlockTexture(faceOverride);
        }

        renderBlocks.setRenderBoundsFromBlock(block);
        renderBlocks.renderStandardBlock(block, x, y, z);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            for (int r = 0; r < 4; r++) {
                if (!WorldUtility.isEnabledSide(sideMap, direction)) {
                    ForgeDirection absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(direction.ordinal(), r));

                    if (!WorldUtility.isEnabledSide(sideMap, absDir)) {
                        RenderUtility.rotateFacesOnRenderer(absDir, renderBlocks, true);
                        tessellateFace(renderBlocks, access, x, y, z, block, edgeOverride, direction.ordinal());
                        RenderUtility.resetFacesOnRenderer(renderBlocks);
                    }
                }
            }
        }

        renderBlocks.clearOverrideBlockTexture();
    }

    /**
     * For rendering items.
     */
    public static void tessellateBlockWithConnectedTextures(int metadata, Block block, IIcon faceOverride, IIcon edgeOverride) {
        GL11.glPushMatrix();

        RenderBlocks renderBlocks = RenderUtility.renderBlocks;
        renderBlocks.setOverrideBlockTexture(faceOverride);
        GL11.glPushMatrix();
        GL11.glScaled(0.999, 0.999, 0.999);
        RenderUtility.renderNormalBlockAsItem(block, metadata, renderBlocks);
        Tessellator.instance.startDrawingQuads();
        GL11.glPopMatrix();
        GL11.glTranslated(-0.5, -0.5, -0.5);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            for (int i = 0; i < 4; i++) {
                ForgeDirection absDir = ForgeDirection.getOrientation(RotationUtility.rotateSide(direction.ordinal(), i));
                RenderUtility.rotateFacesOnRenderer(absDir, renderBlocks, true);
                tessellateFace(renderBlocks, block, edgeOverride, direction.ordinal());
                RenderUtility.resetFacesOnRenderer(renderBlocks);
            }
        }

        Tessellator.instance.draw();
        GL11.glPopMatrix();
    }
}