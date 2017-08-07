package org.halvors.quantum.client.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtility {
    public static void bindTexture(ResourceLocation location) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
    }

    public static void rotateBlockBasedOnDirection(EnumFacing direction) {
        switch (direction) {
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

    public static void renderParticle(Particle particle) {
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }

    public static void renderText(String text, EnumFacing side, float maxScale, double x, double y, double z) {
        GlStateManager.pushMatrix();

        GlStateManager.doPolygonOffset(-10, -10);
        GlStateManager.enablePolygonOffset();

        float displayWidth = 1;
        float displayHeight = 1;
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

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

        int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        int requiredHeight = fontRenderer.FONT_HEIGHT + 2;
        float scaler = 0.8F;
        float scaleX = (displayWidth / requiredWidth);
        float scaleY = (displayHeight / requiredHeight);
        float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GlStateManager.scale(scale, -scale, scale);
        GlStateManager.depthMask(false);

        int offsetX;
        int offsetY;
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);

        offsetX = (realWidth - requiredWidth) / 2;
        offsetY = (realHeight - requiredHeight) / 2;

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
}
