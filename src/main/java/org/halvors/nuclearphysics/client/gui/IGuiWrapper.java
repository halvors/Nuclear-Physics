package org.halvors.nuclearphysics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IIcon;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IGuiWrapper {
    void drawTexturedRect(int x, int y, int u, int v, int w, int h);

    void drawTexturedRectFromIcon(int x, int y, IIcon icon, int w, int h);

    void displayTooltip(String text, int xAxis, int yAxis);

    void displayTooltips(List<String> list, int xAxis, int yAxis);

    FontRenderer getFontRenderer();
}
