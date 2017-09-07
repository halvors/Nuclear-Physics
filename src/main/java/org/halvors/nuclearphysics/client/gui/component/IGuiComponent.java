package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IGuiComponent {
	void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight);

	void renderForeground(int xAxis, int yAxis);

	void preMouseClicked(int xAxis, int yAxis, int button);

	void mouseClicked(int xAxis, int yAxis, int button);

	void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

	void mouseReleased(int x, int y, int type);

	void mouseWheel(int x, int y, int delta);
}