package org.halvors.quantum.client.gui.component;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.IGui;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.ConfigurationManager.Client;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.utility.energy.EnergyUnit;

@SideOnly(Side.CLIENT)
public class GuiEnergyUnitType extends GuiComponentBase implements IGuiComponent {
	public GuiEnergyUnitType(IGui gui, ResourceLocation defaultResource) {
		super("EnergyUnitType.png", gui, defaultResource);
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin + guiWidth, yOrigin + 2, 0, 0, 26, 26);

        int x = guiWidth + 4;
        int y = 2 + 5;
		int renderX = 26 + (18 * Client.energyUnit.ordinal());

		if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
			gui.drawTexturedRect(xOrigin + x - 1, yOrigin + y - 1, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(xOrigin + x - 1, yOrigin + y - 1, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);

        int x = guiWidth + 4;
        int y = 2 + 5;

        if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
			displayTooltip(Client.energyUnit.getName(), xAxis, yAxis);
		}

		super.renderForeground(xAxis, yAxis, guiWidth, guiHeight);
	}

	@Override
	public void preMouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {

	}

	@Override
	public void mouseClicked(int xAxis, int yAxis, int guiWidth, int guiHeight, int button) {
		switch (button) {
			case 0:
                int x = guiWidth + 4;
                int y = 2 + 5;

                if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
					int ordinalToSet = Client.energyUnit.ordinal() < (EnergyUnit.values().length - 1) ? Client.energyUnit.ordinal() + 1 : 0;

					//SoundHandler.playSound("gui.button.press");

					// Set energy unit type to use, and save the configuration.
					Client.energyUnit = EnergyUnit.values()[ordinalToSet];

					// Save this change to the configuration.
					ConfigurationManager.saveConfiguration(Quantum.getConfiguration());
				}
				break;
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

	}

	@Override
	public void mouseReleased(int x, int y, int type) {

	}
}
