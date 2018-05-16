package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiBar extends GuiComponent {
    private final EnumBarType type;
    private final IBarInfoHandler barInfoHandler;

    public GuiBar(final IBarInfoHandler barInfoHandler, final EnumBarType type, final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "bar.png"), gui, x, y);

        this.type = type;
        this.barInfoHandler = barInfoHandler;
    }

    public GuiBar(final IEnergyStorage energyStorage, final IGuiWrapper gui, final int x, final int y) {
        this(new IBarInfoHandler() {
            @Override
            public double getLevel() {
                return energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
            }

            @Override
            public String getTooltip() {
                return UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored());
            }
        }, EnumBarType.POWER, gui, x, y);
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, type.getWidth(), type.getHeight());
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.getTextureX(), type.getTextureY(), type.getWidth(), type.getHeight());

        final double level = barInfoHandler.getLevel();

        if (level > 0) {
            if (type.isHorizontal()) {
                int scale = (int) (level * type.getWidth());

                gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.getTextureX() + type.getWidth(), type.getTextureY(), scale, type.getHeight());
            } else {
                int scale = (int) (level * type.getHeight());

                gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation + type.getHeight() - scale, type.getTextureX() + type.getWidth(), type.getTextureY() + type.getHeight() - scale, type.getWidth(), scale);
            }
        }

        RenderUtility.bindTexture(resource);
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        RenderUtility.bindTexture(resource);

        final String tooltip = barInfoHandler.getTooltip();

        if (tooltip != null && !tooltip.isEmpty() && xAxis >= xLocation && xAxis <= xLocation + type.getWidth() && yAxis >= yLocation && yAxis <= yLocation + type.getHeight()) {
            displayTooltip(tooltip, xAxis, yAxis);
        }

        RenderUtility.bindTexture(resource);
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
        TIMER(64, 11, 0, 11),
        PROGRESS(22, 16, 0, 22),
        POWER(6, 49, 0, 38, false);

        private final int width;
        private final int height;
        private final int textureX;
        private final int textureY;
        private boolean horizontal;

        EnumBarType(final int width, final int height, final int x, final int y, final boolean horizontal) {
            this.width = width;
            this.height = height;
            this.textureX = x;
            this.textureY = y;
            this.horizontal = horizontal;
        }

        EnumBarType(final int width, final int height, final int x, final int y) {
            this(width, height, x, y, true);
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

        public boolean isHorizontal() {
            return horizontal;
        }
    }
}
