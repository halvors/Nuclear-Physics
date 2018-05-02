package org.halvors.nuclearphysics.client.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtility {
    public static void bindTexture(ResourceLocation location) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
    }

    public static void rotateBlockBasedOnDirection(final EnumFacing facing) {
        switch (facing) {
            case SOUTH:
                GlStateManager.translate(1, 0, 1);
                GlStateManager.rotate(180, 0, 1, 0);
                break;

            case WEST:
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(90, 0, 1, 0);
                break;

            case EAST:
                GlStateManager.translate(1, 0, 0);
                GlStateManager.rotate(270, 0, 1, 0);
                break;
        }
    }

    public static void renderParticle(final Particle particle) {
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void renderFloatingText(final String text, final BlockPos pos) {
        renderFloatingText(text, pos, 0xFFFFFF);
    }

    public static void renderFloatingText(final String text, final BlockPos pos, final int color) {
        renderFloatingText(text, pos.getX(), pos.getY(), pos.getZ(), color);
    }

    public static void renderFloatingText(final String text, final double x, final double y, final double z, final int color) {
        final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final float scale = 0.027F;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0, 1, 0);
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        final int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        final int yOffset = 0;

        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(-stringMiddle - 1, -1 + yOffset, 0).color(0, 0, 0, 0.25F).endVertex();
        bufferBuilder.pos(-stringMiddle - 1, 8 + yOffset, 0).color(0, 0, 0, 0.25F).endVertex();
        bufferBuilder.pos(stringMiddle + 1, 8 + yOffset, 0).color(0, 0, 0, 0.25F).endVertex();
        bufferBuilder.pos(stringMiddle + 1, -1 + yOffset, 0).color(0, 0, 0, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontRenderer.drawString(text, -stringMiddle, yOffset, color);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRenderer.drawString(text, -stringMiddle, yOffset, color);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static void renderText(final String text, final EnumFacing side, final float maxScale, final double x, final double y, final double z) {
        GlStateManager.pushMatrix();

        GlStateManager.doPolygonOffset(-10, -10);
        GlStateManager.enablePolygonOffset();

        final float displayWidth = 1;
        final float displayHeight = 1;
        GlStateManager.translate(x, y, z);
        GlStateManager.pushMatrix();

        switch (side) {
            case SOUTH:
                GlStateManager.translate(0, 1, 0);
                GlStateManager.rotate(0, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;

            case NORTH:
                GlStateManager.translate(1, 1, 1);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;

            case EAST:
                GlStateManager.translate(0, 1, 1);
                GlStateManager.rotate(90, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;

            case WEST:
                GlStateManager.translate(1, 1, 0);
                GlStateManager.rotate(-90, 0, 1, 0);
                GlStateManager.rotate(90, 1, 0, 0);
                break;
        }

        // Find Center
        GlStateManager.translate(displayWidth / 2, 1F, displayHeight / 2);
        GlStateManager.rotate(-90, 1, 0, 0);

        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        final int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        final int requiredHeight = fontRenderer.FONT_HEIGHT + 2;
        final float scaler = 0.8F;
        final float scaleX = (displayWidth / requiredWidth);
        final float scaleY = (displayHeight / requiredHeight);
        final float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GlStateManager.scale(scale, -scale, scale);
        GlStateManager.depthMask(false);

        final int realHeight = (int) Math.floor(displayHeight / scale);
        final int realWidth = (int) Math.floor(displayWidth / scale);
        final int offsetX = (realWidth - requiredWidth) / 2;
        final int offsetY = (realHeight - requiredHeight) / 2;

        GlStateManager.disableLighting();
        fontRenderer.drawString(text, offsetX - (realWidth / 2), 1 + offsetY - (realHeight / 2), 1);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.disablePolygonOffset();

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public static void enableLightmap() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void color(final int color) {
        final float cR = (color >> 16 & 0xFF) / 255.0F;
        final float cG = (color >> 8 & 0xFF) / 255.0F;
        final float cB = (color & 0xFF) / 255.0F;

        GlStateManager.color(cR, cG, cB);
    }
}
