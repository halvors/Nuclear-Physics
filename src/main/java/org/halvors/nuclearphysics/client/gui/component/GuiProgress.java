package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class GuiProgress extends GuiComponent {
    private static final int width = 22;
    private static final int height = 16;

    private IProgressInfoHandler progressInfoHandler;
    private int xLocation;
    private int yLocation;

    public GuiProgress(IProgressInfoHandler progressInfoHandler, IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "progress.png"), gui);

        this.progressInfoHandler = progressInfoHandler;

        this.xLocation = x;
        this.yLocation = y;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, width, height);

        int display = (int) (progressInfoHandler.getProgress() * (width + 1));

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, display, height);
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
}
