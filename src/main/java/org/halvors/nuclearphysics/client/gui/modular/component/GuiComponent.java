package org.halvors.nuclearphysics.client.gui.modular.component;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.IGuiWrapper;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponent implements IGuiComponent {
    protected ResourceLocation resource;
    protected IGuiWrapper gui;
    protected ResourceLocation defaultLocation = ResourceUtility.getResource(ResourceType.GUI, "gui_base.png");

    public GuiComponent(ResourceLocation resource, IGuiWrapper gui) {
        this.resource = resource;
        this.gui = gui;
    }

    public void displayTooltip(String s, int xAxis, int yAxis) {
        gui.displayTooltip(s, xAxis, yAxis);
    }

    public void displayTooltips(List<String> list, int xAxis, int yAxis) {
        gui.displayTooltips(list, xAxis, yAxis);
    }

    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = gui.getFont().getStringWidth(text);

        if (length <= maxX) {
            gui.getFont().drawString(text, x, y, color);
        } else {
            float scale = (float)maxX/length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;

            GlStateManager.pushMatrix();

            GlStateManager.scale(scale, scale, scale);
            gui.getFont().drawString(text, (int)(x*reverse), (int)((y*reverse)+yAdd), color);

            GlStateManager.popMatrix();
        }
    }

    public abstract Rectangle4i getBounds(int guiWidth, int guiHeight);

    public static class Rectangle4i {
        public int x, y, width, height;

        public Rectangle4i(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Rectangle toRectangle() {
            return new Rectangle(x, y, width, height);
        }
    }
}
