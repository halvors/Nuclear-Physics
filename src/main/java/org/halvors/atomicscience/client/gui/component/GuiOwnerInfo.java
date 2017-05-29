package org.halvors.atomicscience.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.electrometrics.client.gui.IGui;

@SideOnly(Side.CLIENT)
public class GuiOwnerInfo extends GuiComponentBase implements IGuiComponent {
	private final IInfoHandler infoHandler;

	public GuiOwnerInfo(IInfoHandler infoHandler, IGui gui, ResourceLocation defaultResource) {
		super("OwnerInfo.png", gui, defaultResource);

		this.infoHandler = infoHandler;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin - 26, yOrigin + 2, 0, 0, 26, 26);

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		int x = -26 + 3;
		int y = 2 + 3;

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