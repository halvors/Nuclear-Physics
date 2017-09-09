package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public abstract class GuiGauge extends GuiComponent {
    private static final int width = 14;
    protected static final int height = 49;

    public GuiGauge(IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "gauge.png"), gui, x, y);
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, width, height);

        TextureAtlasSprite texture = getTexture();
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
    public void renderForeground(int xAxis, int yAxis) {
        if (xAxis >= xLocation + 1 && xAxis <= xLocation + width - 1 && yAxis >= yLocation + 1 && yAxis <= yLocation + height - 1) {
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

    public abstract int getScaledLevel();

    public abstract TextureAtlasSprite getTexture();

    public abstract String getTooltip();
}
