package org.halvors.nuclearphysics.client.utility;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class RenderUtility {
    /** Icon loading map for external icon registration. */
    public static final HashMap<String, IIcon> loadedIconMap = new HashMap<>();
    public static RenderBlocks renderBlocks = new RenderBlocks();

    public static void renderNormalBlockAsItem(Block block, int metadata, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void rotateFacesOnRenderer(ForgeDirection rotation, RenderBlocks renderer, boolean fullRotation) {
        if (fullRotation) {
            switch (rotation) {
                case DOWN:
                    renderer.uvRotateSouth = 3;
                    renderer.uvRotateNorth = 3;
                    renderer.uvRotateEast = 3;
                    renderer.uvRotateWest = 3;
                    break;

                case EAST:
                    renderer.uvRotateTop = 1;
                    renderer.uvRotateBottom = 2;
                    renderer.uvRotateWest = 1;
                    renderer.uvRotateEast = 2;
                    break;

                case NORTH:
                    renderer.uvRotateNorth = 2;
                    renderer.uvRotateSouth = 1;
                    break;

                case SOUTH:
                    renderer.uvRotateTop = 3;
                    renderer.uvRotateBottom = 3;
                    renderer.uvRotateNorth = 1;
                    renderer.uvRotateSouth = 2;
                    break;

                case UNKNOWN:
                    break;

                case UP:
                    break;

                case WEST:
                    renderer.uvRotateTop = 2;
                    renderer.uvRotateBottom = 1;
                    renderer.uvRotateWest = 2;
                    renderer.uvRotateEast = 1;
                    break;

            }
        } else {
            switch (rotation) {
                case EAST:
                    renderer.uvRotateTop = 1;
                    break;
                case WEST:
                    renderer.uvRotateTop = 2;
                    break;
                case SOUTH:
                    renderer.uvRotateTop = 3;
                    break;
                default:
                    break;
            }
        }
    }

    public static void resetFacesOnRenderer(RenderBlocks renderer) {
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateWest = 0;
        renderer.flipTexture = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void bindTexture(ResourceLocation location) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
    }

    public static void rotateBlockBasedOnDirection(final ForgeDirection facing) {
        switch (facing) {
            case SOUTH:
                GL11.glTranslated(1, 0, 1);
                GL11.glRotated(180, 0, 1, 0);
                break;

            case WEST:
                GL11.glTranslated(0, 0, 1);
                GL11.glRotated(90, 0, 1, 0);
                break;

            case EAST:
                GL11.glTranslated(1, 0, 0);
                GL11.glRotated(270, 0, 1, 0);
                break;
        }
    }

    public static void renderFloatingText(final String text, final BlockPos position) {
        renderFloatingText(text, position, 0xFFFFFF);
    }

    /** Renders a floating text in a specific position.
     *
     * @author Briman0094 */
    public static void renderFloatingText(final String text, final BlockPos position, final int color) {
        renderFloatingText(text, position.getX(), position.getY(), position.getZ(), color);
    }

    public static void renderFloatingText(final String text, final double x, final double y, final double z, final int color) {
        final RenderManager renderManager = RenderManager.instance;
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        final float scale = 0.027F;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0, 1, 0);
        GL11.glRotated(-renderManager.playerViewY, 0, 1, 0);
        GL11.glRotated(renderManager.playerViewX, 1, 0, 0);
        GL11.glScaled(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        final int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        final int yOffset = 0;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0, 0, 0, 0.5F);
        tessellator.addVertex(-stringMiddle - 1, -1 + yOffset, 0);
        tessellator.addVertex(-stringMiddle - 1, 8 + yOffset, 0);
        tessellator.addVertex(stringMiddle + 1, 8 + yOffset, 0);
        tessellator.addVertex(stringMiddle + 1, -1 + yOffset, 0);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontRenderer.drawString(text, -stringMiddle, yOffset, color);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH);
        GL11.glDepthMask(true);
        fontRenderer.drawString(text, -stringMiddle, yOffset, color);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4d(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    public static void renderText(final String text, final ForgeDirection side, final float maxScale, final double x, final double y, final double z) {
        GL11.glPushMatrix();

        GL11.glPolygonOffset(-10, -10);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);

        final float displayWidth = 1;
        final float displayHeight = 1;
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();

        switch (side) {
            case SOUTH:
                GL11.glTranslated(0, 1, 0);
                GL11.glRotated(0, 0, 1, 0);
                GL11.glRotated(90, 1, 0, 0);
                break;

            case NORTH:
                GL11.glTranslated(1, 1, 1);
                GL11.glRotated(180, 0, 1, 0);
                GL11.glRotated(90, 1, 0, 0);
                break;

            case EAST:
                GL11.glTranslated(0, 1, 1);
                GL11.glRotated(90, 0, 1, 0);
                GL11.glRotated(90, 1, 0, 0);
                break;

            case WEST:
                GL11.glTranslated(1, 1, 0);
                GL11.glRotated(-90, 0, 1, 0);
                GL11.glRotated(90, 1, 0, 0);
                break;
        }

        // Find Center
        GL11.glTranslated(displayWidth / 2, 1F, displayHeight / 2);
        GL11.glRotated(-90, 1, 0, 0);

        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        final int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        final int requiredHeight = fontRenderer.FONT_HEIGHT + 2;
        final float scaler = 0.8F;
        final float scaleX = (displayWidth / requiredWidth);
        final float scaleY = (displayHeight / requiredHeight);
        final float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GL11.glScaled(scale, -scale, scale);
        GL11.glDepthMask(false);

        final int realHeight = (int) Math.floor(displayHeight / scale);
        final int realWidth = (int) Math.floor(displayWidth / scale);
        final int offsetX = (realWidth - requiredWidth) / 2;
        final int offsetY = (realHeight - requiredHeight) / 2;

        GL11.glDisable(GL11.GL_LIGHTING);
        fontRenderer.drawString(text, offsetX - (realWidth / 2), 1 + offsetY - (realHeight / 2), 1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public static void enableLightmap() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void color(final int color) {
        final float cR = (color >> 16 & 0xFF) / 255.0F;
        final float cG = (color >> 8 & 0xFF) / 255.0F;
        final float cB = (color & 0xFF) / 255.0F;

        GL11.glColor3f(cR, cG, cB);
    }
}
