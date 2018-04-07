package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.sound.SoundHandler;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketRedstoneControl;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.type.RedstoneControl;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiRedstoneControl extends GuiComponent {
    private TileEntity tile;

    public <T extends TileEntity & ITileRedstoneControl> GuiRedstoneControl(T tile, IGuiWrapper gui, int x, int y) {
        super(ResourceUtility.getResource(Resource.GUI_COMPONENT, "redstone_control.png"), gui, x, y);

        this.tile = tile;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, 26, 26);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, 26, 26);

        int renderX = 26 + (20 * ((ITileRedstoneControl) tile).getRedstoneControl().ordinal());

        if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
            gui.drawTexturedRect(guiWidth + xLocation + 2, guiHeight + yLocation + 3, renderX, 0, 20, 20);
        } else {
            gui.drawTexturedRect(guiWidth + xLocation + 2, guiHeight + yLocation + 3, renderX, 20, 20, 20);
        }
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        RenderUtility.bindTexture(resource);

        if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
            displayTooltip(((ITileRedstoneControl) tile).getRedstoneControl().getDisplay(), xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        switch (button) {
            case 0:
                if (isPointInRegion(xLocation + 2, yLocation + 3, xAxis, yAxis, 20, 20)) {
                    ITileRedstoneControl tileRedstoneControl = (ITileRedstoneControl) tile;
                    RedstoneControl redstoneControl = tileRedstoneControl.getRedstoneControl();
                    int ordinalToSet = redstoneControl.ordinal() < (RedstoneControl.values().length - 1) ? redstoneControl.ordinal() + 1 : 0;

                    if (ordinalToSet == RedstoneControl.PULSE.ordinal() && !tileRedstoneControl.canPulse()) {
                        ordinalToSet = 0;
                    }

                    SoundHandler.playSound("gui.button.press");
                    NuclearPhysics.getPacketHandler().sendToServer(new PacketRedstoneControl(new Position(tile.xCoord, tile.yCoord, tile.zCoord), RedstoneControl.values()[ordinalToSet]));
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

    @Override
    public void mouseWheel(int x, int y, int delta) {

    }
}
