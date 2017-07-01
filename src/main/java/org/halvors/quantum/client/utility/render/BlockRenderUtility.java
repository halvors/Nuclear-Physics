package org.halvors.quantum.client.utility.render;

/**
 * A block rendering helper class.
 */
/*
public class BlockRenderUtility {
    public static void setupLight(World world, int x, int y, int z) {
        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube()) {
            return;
        }

        int br = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int var11 = br % 65536;
        int var12 = br / 65536;
        float scale = 1;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var11 * scale, var12 * scale);
    }

    public static void tessellateFace(RenderBlocks renderBlocks, Block block, IIcon overrideTexture, int side) {
        Tessellator tessellator = Tessellator.getInstance();
        int metadata = 0;
        IIcon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(side, metadata);

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
/*
    public static void tessellateBlockWithConnectedTextures(byte sideMap, IBlockAccess access, int x, int y, int z, Block block, IIcon faceOverride, IIcon edgeOverride) {
        tessellateBlockWithConnectedTextures(sideMap, RenderUtility.renderBlocks, access, x, y, z, block, faceOverride, edgeOverride);
    }

    public static void tessellateBlockWithConnectedTextures(byte sideMap, RenderBlocks renderBlocks, IBlockAccess access, int x, int y, int z, Block block, IIcon faceOverride, IIcon edgeOverride) {
        renderBlocks.blockAccess = access;

        if (faceOverride != null)
        renderBlocks.setOverrideBlockTexture(faceOverride);

        renderBlocks.setRenderBoundsFromBlock(block);
        renderBlocks.renderStandardBlock(block, x, y, z);

        for (EnumFacing direction : EnumFacing.VALUES) {
            for (int side = 0; side <= 4; side++) {
                if (!WorldUtility.isEnabledSide(sideMap, direction)) {
                    EnumFacing absDirection = EnumFacing.getOrientation(RotationUtility.rotateSide(direction.ordinal(), side));

                    if (!WorldUtility.isEnabledSide(sideMap, absDirection)) {
                        RenderUtility.rotateFacesOnRenderer(absDirection, renderBlocks, true);
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
/*
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

        for (EnumFacing direction : EnumFacing.VALUES) {
            for (int side = 0; side <= 4; side++) {
                EnumFacing absDirection = EnumFacing.getOrientation(RotationUtility.rotateSide(direction.ordinal(), side));
                RenderUtility.rotateFacesOnRenderer(absDirection, renderBlocks, true);
                tessellateFace(renderBlocks, block, edgeOverride, direction.ordinal());
                RenderUtility.resetFacesOnRenderer(renderBlocks);
            }
        }

        Tessellator.getInstance().draw();
        GL11.glPopMatrix();
    }
}
*/