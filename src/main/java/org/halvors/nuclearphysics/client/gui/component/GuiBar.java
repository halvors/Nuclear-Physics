package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiBar extends GuiComponent {
    private final IProgressInfoHandler progressInfoHandler;
    private final EnumBarType type;

    public GuiBar(final IProgressInfoHandler progressInfoHandler, final EnumBarType type, final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "bar.png"), gui, x, y);

        this.progressInfoHandler = progressInfoHandler;
        this.type = type;
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, type.getWidth(), type.getHeight());
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.getTextureX(), type.getTextureY(), type.getWidth(), type.getHeight());

        final int scale = (int) (progressInfoHandler.getProgress() * type.getWidth());

        if (scale > 0) {
            RenderUtility.bindTexture(resource);

            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.getTextureX() + type.getWidth(), type.getTextureY(), scale, type.getHeight());
        }
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {

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

    public enum EnumBarType {
        TEMPERATURE(64, 11, 0, 0),
        TIMER(64, 11, 0, 11);

        private final int width;
        private final int height;
        private final int textureX;
        private final int textureY;

        EnumBarType(int w, int h, int x, int y) {
            this.width = w;
            this.height = h;
            this.textureX = x;
            this.textureY = y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getTextureX() {
            return textureX;
        }

        public int getTextureY() {
            return textureY;
        }
    }
}
