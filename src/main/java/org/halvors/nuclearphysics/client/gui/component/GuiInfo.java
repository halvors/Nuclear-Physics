package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiInfo extends GuiComponent {
    private IInfoHandler infoHandler;

    public GuiInfo(IInfoHandler infoHandler, ResourceLocation resource, IGuiWrapper gui, int x, int y) {
        super(resource, gui, x, y);

        this.infoHandler = infoHandler;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, 26, 26);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, 26, 26);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (isPointInRegion(xLocation + 3, yLocation + 3, xAxis, yAxis, 21, 20)) {
            displayTooltips(getInfo(infoHandler.getInfo()), xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        switch (button) {
            case 0:
                if (isPointInRegion(xLocation + 3, yLocation + 3, xAxis, yAxis, 21, 20)) {
                    buttonClicked();
                }
                break;
        }
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

    protected abstract List<String> getInfo(List<String> list);

    protected abstract void buttonClicked();
}
