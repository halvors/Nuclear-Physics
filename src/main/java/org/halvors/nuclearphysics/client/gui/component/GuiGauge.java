package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public abstract class GuiGauge extends GuiComponent {
    private static final int WIDTH = 14;
    protected static final int HEIGHT = 49;

    public GuiGauge(final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "gauge.png"), gui, x, y);
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, WIDTH, HEIGHT);
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, WIDTH, HEIGHT);

        final IIcon texture = getTexture();
        int scale = getScaledLevel();

        if (texture != null && scale > 0) {
            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, WIDTH, 0, WIDTH, HEIGHT);

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

                RenderUtility.bindTexture(TextureMap.locationBlocksTexture);

                gui.drawTexturedRectFromIcon(guiWidth + xLocation + 1, guiHeight + yLocation + HEIGHT - renderRemaining - start - 1, texture, WIDTH - 2, renderRemaining);

                start += 16;

                if (scale == 0) {
                    break;
                }
            }
        }

        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, WIDTH, 0, WIDTH, HEIGHT);
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        if (isPointInRegion(xLocation, yLocation, xAxis, yAxis, WIDTH, HEIGHT)) {
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

    protected abstract IIcon getTexture();

    protected abstract String getTooltip();
}
