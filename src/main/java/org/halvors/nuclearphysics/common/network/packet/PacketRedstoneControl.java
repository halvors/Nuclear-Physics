package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.type.RedstoneControl;

public class PacketRedstoneControl extends PacketLocation implements IMessage {
    public RedstoneControl redstoneControl;

    public PacketRedstoneControl() {

    }

    public PacketRedstoneControl(Position pos, RedstoneControl redstoneControl) {
        super(pos);

        this.redstoneControl = redstoneControl;
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(redstoneControl.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        redstoneControl = RedstoneControl.values()[dataStream.readInt()];
    }

    public static class PacketRedstoneControlMessage implements IMessageHandler<PacketRedstoneControl, IMessage> {
        @Override
        public IMessage onMessage(PacketRedstoneControl message, MessageContext messageContext) {
            final World world = PacketHandler.getWorld(messageContext);
            final TileEntity tile = world.getTileEntity(message.getX(), message.getY(), message.getZ());

            if (tile instanceof ITileRedstoneControl) {
                ((ITileRedstoneControl) tile).setRedstoneControl(message.redstoneControl);
            }

            return null;
        }
    }
}
