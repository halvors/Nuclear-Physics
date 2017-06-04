package org.halvors.quantum.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.client.gui.IGui;
import org.halvors.quantum.client.sound.SoundHandler;
import org.halvors.quantum.common.base.RedstoneControlType;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.base.tile.ITileRedstoneControl;
import org.halvors.quantum.common.network.NetworkHandler;
import org.halvors.quantum.common.network.packet.PacketTileRedstoneControl;
import org.halvors.quantum.common.tile.TileEntity;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl<T extends TileEntity & ITileNetworkable & ITileRedstoneControl> extends GuiComponentBase implements IGuiComponent {
	private final T tileEntity;

	public GuiRedstoneControl(IGui gui, T tileEntity, ResourceLocation defaultResource) {
		super("RedstoneControl.png", gui, defaultResource);

		this.tileEntity = tileEntity;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidth, int guiHeight) {
		game.renderEngine.bindTexture(resource);
		gui.drawTexturedRect(xOrigin + guiWidth, yOrigin + guiHeight - 2 - 26, 0, 0, 26, 26);

		int x = guiWidth + 4;
		int y = guiHeight - 2 - 26 + 5;
        int renderX = 26 + (18 * tileEntity.getControlType().ordinal());

        if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
			gui.drawTexturedRect(xOrigin + x - 1, yOrigin + y - 1, renderX, 0, 18, 18);
		} else {
			gui.drawTexturedRect(xOrigin + x - 1, yOrigin + y - 1, renderX, 18, 18, 18);
		}

		super.renderBackground(xAxis, yAxis, xOrigin, yOrigin, guiWidth, guiHeight);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
		int x = guiWidth + 4;
		int y = guiHeight - 2 - 26 + 5;

		game.renderEngine.bindTexture(resource);

		if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
			displayTooltip(tileEntity.getControlType().getDisplay(), xAxis, yAxis);
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
				int y = guiHeight - 2 - 26 + 5;

				if (xAxis >= x && xAxis <= x + 15 && yAxis >= y && yAxis <= y + 15) {
					RedstoneControlType current = tileEntity.getControlType();
					int ordinalToSet = current.ordinal() < (RedstoneControlType.values().length - 1) ? current.ordinal() + 1 : 0;

					if (ordinalToSet == RedstoneControlType.PULSE.ordinal() && !tileEntity.canPulse()) {
						ordinalToSet = 0;
					}

					SoundHandler.playSound("gui.button.press");

					// Send a update packet to the server.
					NetworkHandler.sendToServer(new PacketTileRedstoneControl(tileEntity, PacketTileRedstoneControl.PacketType.UPDATE, RedstoneControlType.values()[ordinalToSet]));
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
