package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class GuiSlot extends GuiComponent {
    private int xLocation;
    private int yLocation;

    private String tooltip;

    private int width;
    private int height;
    private int textureX;
    private int textureY;

    public GuiSlot(SlotType type, IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "slot.png"), gui);

        this.xLocation = x;
        this.yLocation = y;

        this.width = type.width;
        this.height = type.height;
        this.textureX = type.textureX;
        this.textureY = type.textureY;
    }

    public GuiSlot(SlotType type, IGuiWrapper gui, int x, int y, String tooltip) {
        this(type, gui, x, y);

        this.tooltip = tooltip;
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, textureX, textureY, width, height);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (xAxis >= xLocation + 1 && xAxis <= xLocation + width - 1 && yAxis >= yLocation + 1 && yAxis <= yLocation + height - 1) {
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

    public enum SlotType {
        NORMAL(18, 18, 0, 0),
        BATTERY(18, 18, 18, 0),
        LIQUID(18, 18, 36, 0),
        GAS(18, 18, 54, 0);

        public int width;
        public int height;
        public int textureX;
        public int textureY;

        SlotType(int w, int h, int x, int y) {
            this.width = w;
            this.height = h;
            this.textureX = x;
            this.textureY = y;
        }
    }
}
