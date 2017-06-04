package org.halvors.quantum.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.quantum.common.component.IComponent;

@SideOnly(Side.CLIENT)
public interface IGuiComponent extends IComponent {
    void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight);

	void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight);

	void preMouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button);

	void mouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button);

	void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

	void mouseReleased(int x, int y, int type);
}