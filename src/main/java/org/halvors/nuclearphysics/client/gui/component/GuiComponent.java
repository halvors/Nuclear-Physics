package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponent implements IGuiComponent {
    protected ResourceLocation resource;
    protected IGuiWrapper gui;

    protected int xLocation;
    protected int yLocation;

    public GuiComponent(ResourceLocation resource, IGuiWrapper gui, int x, int y) {
        this.resource = resource;
        this.gui = gui;

        this.xLocation = x;
        this.yLocation = y;
    }

    public void displayTooltip(String s, int xAxis, int yAxis) {
        gui.displayTooltip(s, xAxis, yAxis);
    }

    public void displayTooltips(List<String> list, int xAxis, int yAxis) {
        gui.displayTooltips(list, xAxis, yAxis);
    }

    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = gui.getFontRenderer().getStringWidth(text);

        if (length <= maxX) {
            gui.getFontRenderer().drawString(text, x, y, color);
        } else {
            float scale = (float) maxX / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;

            GL11.glPushMatrix();

            GL11.glScalef(scale, scale, scale);
            gui.getFontRenderer().drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

            GL11.glPopMatrix();
        }
    }

    protected boolean isPointInRegion(int x, int y, int xAxis, int yAxis, int width, int height) {
        return xAxis >= x && xAxis <= x + width - 1 && yAxis >= y && yAxis <= y + height - 1;
    }
}
