package org.halvors.quantum.lib.utility;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Set;

public class RenderUtility {
    public static final ResourceLocation PARTICLE_RESOURCE = new ResourceLocation("textures/particle/particles.png");
    public static final HashMap<String, ResourceLocation> resourceCahce = new HashMap<>();
    public static final HashMap<String, IIcon> loadedIconMap = new HashMap<>();
    public static RenderBlocks renderBlocks = new RenderBlocks();

    public static ResourceLocation getResource(String domain, String name) {
        String cacheName = domain + ":" + name;

        if (!resourceCahce.containsKey(cacheName)) {
            resourceCahce.put(cacheName, new ResourceLocation(domain, name));
        }

        return resourceCahce.get(cacheName);
    }

    public static void setTerrainTexture() {
        setSpriteTexture(0);
    }

    public static void setSpriteTexture(ItemStack itemStack) {
        setSpriteTexture(itemStack.getItemSpriteNumber());
    }

    public static void setSpriteTexture(int sprite) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(getSpriteTexture(sprite));
    }

    public static ResourceLocation getSpriteTexture(int sprite) {
        return FMLClientHandler.instance().getClient().renderEngine.getResourceLocation(sprite);
    }

    public static void registerIcon(String name, TextureMap textureMap) {
        loadedIconMap.put(name, textureMap.registerIcon(name));
    }

    public static IIcon getIcon(String name) {
        return loadedIconMap.get(name);
    }

    public static void enableBlending() {
        GL11.glShadeModel(7425);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void disableBlending() {
        GL11.glShadeModel(7424);
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glDisable(3042);
    }

    public static void enableLighting() {}

    public static void disableLighting() {
        RenderHelper.disableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void disableLightmap() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void enableLightmap() {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(3553);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

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

    public static void renderFloatingText(String text, Vector3 position) {
        renderFloatingText(text, position, 16777215);
    }

    public static void renderFloatingText(String text, Vector3 position, int color) {
        renderFloatingText(text, position.x, position.y, position.z, color);
    }

    public static void renderFloatingText(String text, double x, double y, double z, int color) {
        RenderManager renderManager = RenderManager.instance;
        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float scale = 0.027F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        int yOffset = 0;

        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        int stringMiddle = fontRenderer.getStringWidth(text) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.5F);
        tessellator.addVertex(-stringMiddle - 1, -1 + yOffset, 0.0D);
        tessellator.addVertex(-stringMiddle - 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, 8 + yOffset, 0.0D);
        tessellator.addVertex(stringMiddle + 1, -1 + yOffset, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, yOffset, color);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void renderText(String text, int side, float maxScale, double x, double y, double z) {
        renderText(text, ForgeDirection.getOrientation(side), maxScale, x, y, z);
    }

    public static void renderText(String text, ForgeDirection side, float maxScale, double x, double y, double z) {
        GL11.glPushMatrix();

        GL11.glPolygonOffset(-10.0F, -10.0F);
        GL11.glEnable(32823);

        float displayWidth = 1.0F;
        float displayHeight = 1.0F;
        GL11.glTranslated(x, y, z);
        GL11.glPushMatrix();

        switch (side) {
            case SOUTH:
                GL11.glTranslatef(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

                break;
            case NORTH:
                GL11.glTranslatef(1.0F, 1.0F, 1.0F);
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

                break;
            case EAST:
                GL11.glTranslatef(0.0F, 1.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

                break;
            case WEST:
                GL11.glTranslatef(1.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        }

        GL11.glTranslatef(displayWidth / 2.0F, 1.0F, displayHeight / 2.0F);
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        int lineHeight = fontRenderer.FONT_HEIGHT + 2;
        int requiredHeight = lineHeight * 1;
        float scaler = 0.8F;
        float scaleX = displayWidth / requiredWidth;
        float scaleY = displayHeight / requiredHeight;
        float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GL11.glScalef(scale, -scale, scale);
        GL11.glDepthMask(false);

        int realHeight = (int)Math.floor(displayHeight / scale);
        int realWidth = (int)Math.floor(displayWidth / scale);

        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;

        GL11.glDisable(2896);
        fontRenderer.drawString("�f" + text, offsetX - realWidth / 2, 1 + offsetY - realHeight / 2, 1);
        GL11.glEnable(2896);
        GL11.glDepthMask(true);
        GL11.glDisable(32823);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public static void renderText(String text, float scaler, float maxScale) {
        GL11.glPushMatrix();

        GL11.glPolygonOffset(-10.0F, -10.0F);
        GL11.glEnable(32823);

        float displayWidth = 1.0F;
        float displayHeight = 1.0F;

        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);

        FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRendererObj;

        int requiredWidth = Math.max(fontRenderer.getStringWidth(text), 1);
        int lineHeight = fontRenderer.FONT_HEIGHT;
        int requiredHeight = lineHeight;
        float scaleX = displayWidth / requiredWidth;
        float scaleY = displayHeight / requiredHeight;
        float scale = Math.min(maxScale, Math.min(scaleX, scaleY) * scaler);

        GL11.glScalef(scale, -scale, scale);
        GL11.glDepthMask(false);

        int realHeight = (int)Math.floor(displayHeight / scale);
        int realWidth = (int)Math.floor(displayWidth / scale);

        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;

        GL11.glDisable(2896);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        fontRenderer.drawString("�f" + text, offsetX - realWidth / 2, 2 + offsetY - realHeight / 2, 1);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontRenderer.drawString("�f" + text, offsetX - realWidth / 2, 2 + offsetY - realHeight / 2, 1);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glDisable(32823);

        GL11.glPopMatrix();
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

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation) {
        renderInventoryBlock(renderer, block, rotation, -1);
    }

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier) {
        renderInventoryBlock(renderer, block, rotation, colorMultiplier, null);
    }

    public static void renderInventoryBlock(RenderBlocks renderer, Block block, ForgeDirection rotation, int colorMultiplier, Set<ForgeDirection> enabledSides) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        if (colorMultiplier > -1) {
            float r = (colorMultiplier >> 16 & 0xFF) / 255.0F;
            float g = (colorMultiplier >> 8 & 0xFF) / 255.0F;
            float b = (colorMultiplier & 0xFF) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
        }

        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        int metadata = rotation.ordinal();

        if (enabledSides == null || enabledSides.contains(ForgeDirection.DOWN)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
        }

        if (enabledSides == null || enabledSides.contains(ForgeDirection.UP)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
            tessellator.draw();
        }

        if (enabledSides == null || enabledSides.contains(ForgeDirection.SOUTH)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
            tessellator.draw();
        }

        if (enabledSides == null || enabledSides.contains(ForgeDirection.NORTH)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
            tessellator.draw();
        }

        if (enabledSides == null || enabledSides.contains(ForgeDirection.WEST)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
            tessellator.draw();
        }

        if (enabledSides == null || enabledSides.contains(ForgeDirection.EAST)) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
            tessellator.draw();
        }

        GL11.glPopMatrix();
    }

    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block) {
        renderCube(x1, y1, z1, x2, y2, z2, block, null);
    }

    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, IIcon overrideTexture) {
        renderCube(x1, y1, z1, x2, y2, z2, block, overrideTexture, 0);
    }

    public static void renderCube(double x1, double y1, double z1, double x2, double y2, double z2, Block block, IIcon overrideTexture, int metadata) {
        GL11.glPushMatrix();
        Tessellator t = Tessellator.instance;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        renderBlocks.setRenderBounds(x1, y1, z1, x2, y2, z2);

        t.startDrawingQuads();

        IIcon useTexture = overrideTexture != null ? overrideTexture : block.getIcon(0, metadata);
        t.setNormal(0.0F, -1.0F, 0.0F);
        renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(1, metadata);
        t.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(2, metadata);
        t.setNormal(0.0F, 0.0F, -1.0F);
        renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(3, metadata);
        t.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(4, metadata);
        t.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, useTexture);

        useTexture = overrideTexture != null ? overrideTexture : block.getIcon(5, metadata);
        t.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, useTexture);
        t.draw();

        GL11.glPopMatrix();
    }

    public static void rotateFaceBlockToSide(ForgeDirection placementSide) {
        switch (placementSide) {
            case DOWN:
                GL11.glTranslatef(0.0F, -0.45F, 0.0F);
                break;
            case UP:
                GL11.glTranslatef(0.0F, 0.45F, 0.0F);
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GL11.glTranslatef(0.0F, 0.0F, -0.45F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 0.0F, 0.45F);
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glTranslatef(-0.45F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case EAST:
                GL11.glTranslatef(0.45F, 0.0F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    public static void rotateFaceToSideNoTranslate(ForgeDirection placementSide) {
        switch (placementSide) {
            case DOWN:
                break;
            case UP:
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
                break;
            case NORTH:
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case SOUTH:
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case EAST:
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
    }

    public static void rotateFaceBlockToSideOutwards(ForgeDirection placementSide) {
        switch (placementSide) {
            case DOWN:
                GL11.glTranslatef(0.0F, 0.45F, 0.0F);
                GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);

                break;
            case UP:
                GL11.glTranslatef(0.0F, -0.45F, 0.0F);
                break;
            case NORTH:
                GL11.glTranslatef(0.0F, 0.0F, 0.45F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                break;
            case SOUTH:
                GL11.glTranslatef(0.0F, 0.0F, -0.45F);
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case WEST:
                GL11.glTranslatef(0.45F, 0.0F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                break;
            case EAST:
                GL11.glTranslatef(-0.45F, 0.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        }
    }

    public static void rotateBlockBasedOnDirection(ForgeDirection direction) {
        switch (direction) {
            default:
                GL11.glRotatef(WorldUtility.getAngleFromForgeDirection(direction), 0.0F, 1.0F, 0.0F);
                break;
            case DOWN:
                GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                break;
            case UP:
                GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        }
    }

    public static void rotateBlockBasedOnDirectionUp(ForgeDirection direction) {
        switch (direction) {
            default:
                GL11.glRotatef(WorldUtility.getAngleFromForgeDirection(direction), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
                break;
            case DOWN:
                GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
                break;
        }
    }

    public static void bind(String name) {
        bind(getResource("minecraft", name));
    }

    public static void bind(String domain, String name) {
        bind(getResource(domain, name));
    }

    public static void bind(ResourceLocation location) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(location);
    }
}
