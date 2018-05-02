package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiSlot extends GuiComponent {
    private final EnumSlotType type;
    private String tooltip;

    public GuiSlot(final EnumSlotType type, final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "slot.png"), gui, x, y);

        this.type = type;
    }

    public GuiSlot(final IGuiWrapper gui, final int x, final int y) {
        this(EnumSlotType.NORMAL, gui, x, y);
    }

    public GuiSlot(final EnumSlotType type, final IGuiWrapper gui, final int x, final int y, final String tooltip) {
        this(type, gui, x, y);

        this.tooltip = tooltip;
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, type.getWidth(), type.getHeight());
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, type.getTextureX(), type.getTextureY(), type.getWidth(), type.getHeight());
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        if (isPointInRegion(xLocation, yLocation, xAxis, yAxis, type.width, type.height)) {
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

    public enum EnumSlotType {
        NORMAL(18, 18, 0, 0),
        BATTERY(18, 18, 18, 0),
        LIQUID(18, 18, 36, 0),
        GAS(18, 18, 54, 0);

        private final int width;
        private final int height;
        private final int textureX;
        private final int textureY;

        EnumSlotType(int width, int height, int textureX, int textureY) {
            this.width = width;
            this.height = height;
            this.textureX = textureX;
            this.textureY = textureY;
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
