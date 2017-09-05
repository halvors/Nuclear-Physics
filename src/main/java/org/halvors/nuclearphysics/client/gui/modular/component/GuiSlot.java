package org.halvors.nuclearphysics.client.gui.modular.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class GuiSlot extends GuiComponent {
    protected int xLocation;
    protected int yLocation;

    protected int textureX;
    protected int textureY;
    protected int width;
    protected int height;

    public GuiSlot(SlotType type, IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "slot.png"), gui, ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "slot.png"));

        this.xLocation = x;
        this.yLocation = y;

        this.width = type.width;
        this.height = type.height;
        this.textureX = type.textureX;
        this.textureY = type.textureY;
    }

    public GuiSlot(IGuiWrapper gui, int x, int y) {
        this(SlotType.NONE, gui, x, y);
    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(RESOURCE);

        guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, textureX, textureY, width, height);

        RenderUtility.bindTexture(defaultLocation);
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

    public enum SlotType {
        NONE(18, 18, 0, 0),
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
