package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiPowerBar extends GuiComponent {
    private int xLocation;
    private int yLocation;

    private int width = 6;
    private int height = 56;
    private int innerOffsetY = 2;

    private final IEnergyStorage energyStorage;
    private IPowerInfoHandler handler;

    public GuiPowerBar(final IEnergyStorage energyStorage, final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "power_bar.png"), gui, x, y);

        this.energyStorage = energyStorage;

        this.handler = new IPowerInfoHandler() {
            @Override
            public String getTooltip() {
                return UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored());
            }

            @Override
            public double getLevel() {
                return energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
            }
        };

        xLocation = x;
        yLocation = y;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    public static abstract class IPowerInfoHandler {
        public String getTooltip() {
            return null;
        }

        public abstract double getLevel();
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, width, height);

        if (handler.getLevel() > 0) {
            int displayInt = (int) (handler.getLevel() * 52) + innerOffsetY;
            gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation + height - displayInt, 6, height - displayInt, width, displayInt);
        }

        RenderUtility.bindTexture(resource);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        RenderUtility.bindTexture(resource);

        if (handler.getTooltip() != null && xAxis >= xLocation && xAxis <= xLocation + width && yAxis >= yLocation && yAxis <= yLocation + height) {
            displayTooltip(handler.getTooltip(), xAxis, yAxis);
        }

        RenderUtility.bindTexture(resource);
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
