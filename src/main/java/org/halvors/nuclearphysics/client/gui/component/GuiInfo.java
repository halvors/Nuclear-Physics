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
    private final IInfoHandler infoHandler;

    public GuiInfo(final IInfoHandler infoHandler, final ResourceLocation resource, final IGuiWrapper gui, final int x, final int y) {
        super(resource, gui, x, y);

        this.infoHandler = infoHandler;
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, 26, 26);
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, 26, 26);
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        if (isPointInRegion(xLocation + 3, yLocation + 3, xAxis, yAxis, 21, 20)) {
            displayTooltips(getInfo(infoHandler.getInfo()), xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(final int xAxis, final int yAxis, final int button) {

    }

    @Override
    public void mouseClicked(final int xAxis, final int yAxis, final int button) {
        switch (button) {
            case 0:
                if (isPointInRegion(xLocation + 3, yLocation + 3, xAxis, yAxis, 21, 20)) {
                    buttonClicked();
                }
                break;
        }
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

    protected abstract List<String> getInfo(final List<String> list);

    protected abstract void buttonClicked();
}
