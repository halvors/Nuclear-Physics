package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.sound.SoundHandler;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketRedstoneControl;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.type.RedstoneControl;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl extends GuiComponent {
    private final TileEntity tile;

    public <T extends TileEntity & ITileRedstoneControl> GuiRedstoneControl(final T tile, final IGuiWrapper gui, final int x, final int y) {
        super(ResourceUtility.getResource(Resource.GUI_COMPONENT, "redstone_control.png"), gui, x, y);

        this.tile = tile;
    }

    @Override
    public Rectangle getBounds(final int guiWidth, final int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, 26, 26);
    }

    @Override
    public void renderBackground(final int xAxis, final int yAxis, final int guiWidth, final int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, 26, 26);

        final int renderX = 26 + (20 * ((ITileRedstoneControl) tile).getRedstoneControl().ordinal());

        if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
            gui.drawTexturedRect(guiWidth + xLocation + 2, guiHeight + yLocation + 3, renderX, 0, 20, 20);
        } else {
            gui.drawTexturedRect(guiWidth + xLocation + 2, guiHeight + yLocation + 3, renderX, 20, 20, 20);
        }
    }

    @Override
    public void renderForeground(final int xAxis, final int yAxis) {
        RenderUtility.bindTexture(resource);

        if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
            displayTooltip(((ITileRedstoneControl) tile).getRedstoneControl().getDisplay(), xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(final int xAxis, final int yAxis, final int button) {

    }

    @Override
    public void mouseClicked(final int xAxis, final int yAxis, final int button) {
        switch (button) {
            case 0:
                if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
                    final ITileRedstoneControl tileRedstoneControl = (ITileRedstoneControl) tile;
                    final RedstoneControl redstoneControl = tileRedstoneControl.getRedstoneControl();
                    int ordinalToSet = redstoneControl.ordinal() < (RedstoneControl.values().length - 1) ? redstoneControl.ordinal() + 1 : 0;

                    if (ordinalToSet == RedstoneControl.PULSE.ordinal() && !tileRedstoneControl.canPulse()) {
                        ordinalToSet = 0;
                    }

                    SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
                    NuclearPhysics.getPacketHandler().sendToServer(new PacketRedstoneControl(tile.getPos(), RedstoneControl.values()[ordinalToSet]));
                }

                break;
        }
    }

    @Override
    public void mouseClickMove(final int mouseX, final int mouseY, final int button, final long ticks) {

    }

    @Override
    public void mouseReleased(final int x, final int y, final int type) {

    }

    @Override
    public void mouseWheel(final int x, final int y, final int delta) {

    }
}
