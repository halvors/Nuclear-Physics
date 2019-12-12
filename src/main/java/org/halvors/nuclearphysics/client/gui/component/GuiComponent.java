package org.halvors.nuclearphysics.client.gui.component;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class GuiComponent implements IGuiComponent {
    protected final ResourceLocation resource;
    protected final IGuiWrapper gui;
    protected final int xLocation;
    protected final int yLocation;

    public GuiComponent(final ResourceLocation resource, final IGuiWrapper gui, final int x, final int y) {
        this.resource = resource;
        this.gui = gui;

        this.xLocation = x;
        this.yLocation = y;
    }

    public void displayTooltip(final String tooltip, final int xAxis, final int yAxis) {
        gui.displayTooltip(tooltip, xAxis, yAxis);
    }

    public void displayTooltips(final List<String> list, final int xAxis, final int yAxis) {
        gui.displayTooltips(list, xAxis, yAxis);
    }

    public void renderScaledText(final String text, final int x, final int y, final int color, final int maxX) {
        final int length = gui.getFontRenderer().getStringWidth(text);

        if (length <= maxX) {
            gui.getFontRenderer().drawString(text, x, y, color);
        } else {
            final float scale = (float) maxX / length;
            final float reverse = 1 / scale;
            final float yAdd = 4 - (scale * 8) / 2F;

            GlStateManager.pushMatrix();

            GlStateManager.scaled(scale, scale, scale);
            gui.getFontRenderer().drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

            GlStateManager.popMatrix();
        }
    }

    protected boolean isPointInRegion(final int x, final int y, final int xAxis, final int yAxis, final int width, final int height) {
        return xAxis >= x && xAxis <= x + width - 1 && yAxis >= y && yAxis <= y + height - 1;
    }
}
