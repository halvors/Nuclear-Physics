package org.halvors.quantum.atomic.client.gui.component;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.gui.IGui;
import org.halvors.quantum.atomic.common.utility.energy.EnergyUtils;

@SideOnly(Side.CLIENT)
public class GuiPowerBar extends GuiComponentBase implements IGuiComponent {
	private IPowerInfoHandler powerInfoHandler;
	private int x;
	private int y;

	private int width = 6;
	private int height = 56;
	private int innerOffsetY = 2;

	public GuiPowerBar(IGui gui, IPowerInfoHandler powerInfoHandler, ResourceLocation defaultResource, int x, int y) {
		super("PowerBar.png", gui, defaultResource);

		this.powerInfoHandler = powerInfoHandler;
		this.x = x;
		this.y = y;
	}

	public GuiPowerBar(IGui gui, final IEnergyStorage energyStorage, ResourceLocation defaultResource, int x, int y) {
		this(gui, new IPowerInfoHandler() {
			@Override
			public String getTooltip() {
				return EnergyUtils.getEnergyDisplay(energyStorage.getEnergyStored());
			}

			@Override
			public double getLevel() {
				return energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
			}
		}, defaultResource, x, y);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidthSize, int guiHeightSize) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin + x, yOrigin + y, 0, 0, width, height);

		if (powerInfoHandler.getLevel() > 0) {
			int displayInt = (int) (powerInfoHandler.getLevel() * 52) + innerOffsetY;

			gui.drawTexturedRect(xOrigin + x, yOrigin + y + height - displayInt, 6, height - displayInt, width, displayInt);
		}

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidthSize, guiHeightSize);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int xSize, int ySize) {
		game.renderEngine.bindTexture(resource);

		if (powerInfoHandler.getTooltip() != null && xAxis >= x && xAxis <= x + width && yAxis >= y && yAxis <= y + height) {
			displayTooltip(powerInfoHandler.getTooltip(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, xSize, ySize);
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