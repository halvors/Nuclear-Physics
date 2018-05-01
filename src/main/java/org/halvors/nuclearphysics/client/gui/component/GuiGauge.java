package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public abstract class GuiGauge extends GuiComponent {
    private static final int width = 14;
    protected static final int height = 49;

    public GuiGauge(final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "gauge.png"), gui, x, y);
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, width, height);

        final TextureAtlasSprite texture = getTexture();
        int scale = getScaledLevel();

        if (texture != null && scale > 0) {
            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, width, height);

            int start = 0;

            while (scale > 0) {
                final int renderRemaining;

                if (scale > 16) {
                    renderRemaining = 16;
                    scale -= 16;
                } else {
                    renderRemaining = scale;
                    scale = 0;
                }

                RenderUtility.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                gui.drawTexturedRectFromIcon(guiWidth + xLocation + 1, guiHeight + yLocation + height - renderRemaining - start - 1, texture, width - 2, renderRemaining);

                GlStateManager.resetColor();

                start += 16;

                if (scale == 0) {
                    break;
                }
            }
        }

        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, width, height);
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        if (isPointInRegion(xLocation, yLocation, xAxis, yAxis, width, height)) {
            String tooltip = getTooltip();

            if (tooltip != null && !tooltip.isEmpty()) {
                gui.displayTooltip(tooltip, xAxis, yAxis);
            }
        }
    }

    @Override
    public void preMouseClicked(final int xAxis, final int yAxis, final int button) {

    }

    @Override
    public void mouseClicked(final int xAxis, final int yAxis, final int button) {

    }

    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int button, final long ticks) {

    }

    @Override
    public void mouseReleased(final int x, final int y, final int type) {

    }

    @Override
    public void mouseWheel(final int x, final int y, final int delta) {

    }

    protected abstract int getScaledLevel();

    protected abstract TextureAtlasSprite getTexture();

    protected abstract String getTooltip();
}
