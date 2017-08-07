package org.halvors.quantum.atomic.client.gui.component;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.gui.IGui;

@SideOnly(Side.CLIENT)
public class GuiEnergyInfo extends GuiComponentBase implements IGuiComponent {
	private final IInfoHandler infoHandler;

	public GuiEnergyInfo(IInfoHandler infoHandler, IGui gui, ResourceLocation defaultResource) {
		super("EnergyInfo.png", gui, defaultResource);

		this.infoHandler = infoHandler;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin - 26, yOrigin + guiHeight - 2 - 26, 0, 0, 26, 26);

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		int x = -26 + 3;
		int y = guiHeight - 2 - 26 + 3;

		if (xAxis >= x && xAxis <= x + 21 && yAxis >= y && yAxis <= y + 19) {
			displayTooltips(infoHandler.getInfo(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {

	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

	}

	@Override
	public void mouseReleased(int x, int y, int type) {

	}
}

