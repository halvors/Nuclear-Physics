package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.type.Resource;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiBar extends GuiComponent {
    private IProgressInfoHandler progressInfoHandler;
    private BarType type;

    public GuiBar(IProgressInfoHandler progressInfoHandler, BarType type, IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(Resource.GUI_COMPONENT, "temperature.png"), gui, x, y);

        this.progressInfoHandler = progressInfoHandler;
        this.type = type;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, type.width, type.height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.textureX, type.textureY, type.width, type.height);

        int scale = (int) (progressInfoHandler.getProgress() * type.width);

        if (scale > 0) {
            RenderUtility.bindTexture(resource);

            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.textureX + type.width, type.textureY, scale, type.height);
        }
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {

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

    public enum BarType {
        TEMPERATURE(64, 11, 0, 0),
        TIMER(64, 11, 0, 11);

        public int width;
        public int height;
        public int textureX;
        public int textureY;

        BarType(int w, int h, int x, int y) {
            this.width = w;
            this.height = h;
            this.textureX = x;
            this.textureY = y;
        }
    }
}
