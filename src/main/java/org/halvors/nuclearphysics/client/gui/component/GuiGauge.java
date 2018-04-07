package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public abstract class GuiGauge extends GuiComponent {
    private static final int width = 14;
    protected static final int height = 49;

    public GuiGauge(IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(Resource.GUI_COMPONENT, "gauge.png"), gui, x, y);
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, width, height);

        IIcon texture = getTexture();
        int scale = getScaledLevel();

        if (texture != null && scale > 0) {
            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, width, height);

            int start = 0;

            while (scale > 0) {
                int renderRemaining;

                if (scale > 16) {
                    renderRemaining = 16;
                    scale -= 16;
                } else {
                    renderRemaining = scale;
                    scale = 0;
                }

                RenderUtility.bindTexture(TextureMap.locationBlocksTexture);

                gui.drawTexturedRectFromIcon(guiWidth + xLocation + 1, guiHeight + yLocation + height - renderRemaining - start - 1, texture, width - 2, renderRemaining);

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
    public void renderForeground(int xAxis, int yAxis) {
        if (isPointInRegion(xLocation, yLocation, xAxis, yAxis, width, height)) {
            String tooltip = getTooltip();

            if (tooltip != null && !tooltip.isEmpty()) {
                gui.displayTooltip(tooltip, xAxis, yAxis);
            }
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

    }

    @Override
    public void mouseReleased(int x, int y, int type) {

    }

    @Override
    public void mouseWheel(int x, int y, int delta) {

    }

    protected abstract int getScaledLevel();

    protected abstract IIcon getTexture();

    protected abstract String getTooltip();
}
