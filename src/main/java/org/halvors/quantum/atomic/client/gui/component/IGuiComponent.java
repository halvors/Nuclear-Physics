package org.halvors.quantum.atomic.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.common.tile.component.IComponent;

@SideOnly(Side.CLIENT)
public interface IGuiComponent extends IComponent {
    void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight);

	void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight);

	void preMouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button);

	void mouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button);

	void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

	void mouseReleased(int x, int y, int type);
}