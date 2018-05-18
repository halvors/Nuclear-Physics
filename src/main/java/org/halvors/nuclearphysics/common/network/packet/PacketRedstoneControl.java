package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;

public class PacketRedstoneControl extends PacketLocation implements IMessage {
    public EnumRedstoneControl redstoneControl;

    public PacketRedstoneControl() {

    }

    public PacketRedstoneControl(final BlockPos pos, final EnumRedstoneControl redstoneControl) {
        super(pos);

        this.redstoneControl = redstoneControl;
    }

    @Override
    public void toBytes(final ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(redstoneControl.ordinal());
    }

    @Override
    public void fromBytes(final ByteBuf dataStream) {
        super.fromBytes(dataStream);

        redstoneControl = EnumRedstoneControl.values()[dataStream.readInt()];
    }

    public static class PacketRedstoneControlMessage implements IMessageHandler<PacketRedstoneControl, IMessage> {
        @Override
        public IMessage onMessage(final PacketRedstoneControl message, final MessageContext messageContext) {
            final World world = PacketHandler.getWorld(messageContext);
            final TileEntity tile = message.getPos().getTileEntity(world);

            if (tile instanceof ITileRedstoneControl) {
                ((ITileRedstoneControl) tile).setRedstoneControl(message.redstoneControl);
            }

            return null;
        }
    }
}
