package org.halvors.quantum.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.quantum.client.gui.component.GuiPowerBar;
import org.halvors.quantum.common.base.tile.ITileOwnable;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntityElectricityMeter;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.PlayerUtils;
import org.halvors.quantum.common.utility.energy.EnergyUtils;

/**
 * This is the GUI of the Electricity Meter which provides a simple way to keep count of the electricity you use.
 * Especially suitable for pay-by-use applications where a player buys electricity from another player on multiplayer worlds.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class GuiElectricityMeter extends GuiElectricMachine {
	private final TileEntityElectricityMeter tileEntityElectricityMeter;
	private int ticker = 0;

	public GuiElectricityMeter(final TileEntityElectricityMeter tileEntity) {
		super(tileEntity);

		this.tileEntityElectricityMeter = tileEntity;

		components.add(new GuiPowerBar(this, tileEntity.getStorage(), defaultResource, 164, 15));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		// Create buttons.
		GuiButton resetButton = new GuiButton(0, guiWidth + 6, (guiHeight + ySize) - (20 + 6), 60, 20, LanguageUtility.localize("gui.reset"));

		// If this has a owner, restrict the reset button to that player.
		if (tileEntity instanceof ITileOwnable) {
			ITileOwnable ownable = (ITileOwnable) tileEntity;
			EntityPlayer player = PlayerUtils.getClientPlayer();

			resetButton.enabled = ownable.isOwner(player);
		}

		// Add buttons.
		buttonList.clear();
		buttonList.add(resetButton);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		switch (guiButton.id) {
			case 0:
				// Update the server-side TileEntity.
				NetworkHandler.sendToServer(new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketTileEntityElectricityMeter.PacketType.RESET));
				break;
		}
	}

	@Override
	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		int x = 6 + 12;
		int y = ySize / 2;

		String measured = LanguageUtility.localize("gui.measured");
		String usage = LanguageUtility.localize("gui.usage");
		int xOffset = Math.max(fontRendererObj.getStringWidth(measured), fontRendererObj.getStringWidth(usage)) + 8;

		drawString(measured + ":", x, y - 24);
		drawString(EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityCount()), x + xOffset, y - 24);
		drawString(usage + ":", x, y - 12);
		drawString(EnergyUtils.getEnergyDisplay(tileEntityElectricityMeter.getElectricityUsage()) + "/t", x + xOffset, y - 12);

		if (ticker > 0) {
			ticker--;
		} else {
			ticker = 5;

			// Request the latest data from the server-side TileEntity.
			NetworkHandler.sendToServer(new PacketTileEntityElectricityMeter(tileEntityElectricityMeter, PacketTileEntityElectricityMeter.PacketType.REQUEST));
		}

		super.drawGuiScreenForegroundLayer(mouseX, mouseY);
	}
}