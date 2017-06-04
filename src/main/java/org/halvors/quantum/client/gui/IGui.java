package org.halvors.quantum.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IIcon;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IGui {
	/**
	 * Draws a textured rectangle in this GUI.
	 * @param x
	 * @param y
	 * @param textureX
	 * @param textureY
	 * @param width
	 * @param height
	 */
	void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height);

	/**
	 * Draws a textured rectangle from the specified icon.
	 * @param x
	 * @param y
	 * @param icon
	 * @param width
	 * @param height
	 */
	void drawTexturedRectFromIcon(int x, int y, IIcon icon, int width, int height);

	void drawString(String text, int x, int y);

	/**
	 * Display the specified string as tooltip at the specified location.
	 * @param s
	 * @param xAxis
	 * @param yAxis
	 */
	void displayTooltip(String s, int xAxis, int yAxis);

	/**
	 * Display a list of tooltips at the specified location.
	 * @param list
	 * @param xAxis
	 * @param yAxis
	 */
	void displayTooltips(List<String> list, int xAxis, int yAxis);

	/**
	 * The fontrenderer object of this GUI.
	 * @return FontRenderer
	 */
	FontRenderer getFontRenderer();
}

