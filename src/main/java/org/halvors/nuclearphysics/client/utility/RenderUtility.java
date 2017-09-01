package org.halvors.nuclearphysics.client.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.utility.position.Position;
import org.lwjgl.opengl.GL11;

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

    ///////////////

    public static void renderFloatingText(String text, Position position) {
        renderFloatingText(text, position, 0xFFFFFF);
    }

    /** Renders a floating text in a specific position.
     *
     * @author Briman0094 */
    public static void renderFloatingText(String text, Position position, int color) {
        renderFloatingText(text, position.getX(), position.getY(), position.getZ(), color);
    }

    public static void renderFloatingText(String text, double x, double y, double z, int color) {
        /*
        RenderManager renderManager = RenderManager.instance;
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float scale = 0.027F;

        GlStateManager.color(1, 1, 1, 0.5F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = Tessellator.getInstance().getBuffer();
        int yOffset = 0;

        GlStateManager.disableTexture2D();
        tessellator.startDrawingQuads();
        int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
        tessellator.addVertex(-stringMiddle - 1, -1 + yOffset, 0.0D);
        tessellator.addVertex(-stringMiddle - 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, -1 + yOffset, 0.0D);
        tessellator.draw();
        GlStateManager.enableTexture2D();

        GlStateManager.color(1, 1, 1, 0.5F);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
        */

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0, 1, 0);
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        int yOffset = 0;

        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)(-stringMiddle - 1), (double)(-1 + yOffset), 0).color(0, 0, 0, 0.25F).endVertex();
        vertexbuffer.pos((double)(-stringMiddle - 1), (double)(8 + yOffset), 0).color(0, 0, 0, 0.25F).endVertex();
        vertexbuffer.pos((double)(stringMiddle + 1), (double)(8 + yOffset), 0).color(0, 0, 0, 0.25F).endVertex();
        vertexbuffer.pos((double)(stringMiddle + 1), (double)(-1 + yOffset), 0).color(0, 0, 0, 0.25F).endVertex();
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

    //////////////////////

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

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

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
