package org.halvors.nuclearphysics.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.nuclearphysics.common.NuclearPhysics;
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

            NuclearPhysics.getProxy().addScheduledTask(() -> {
                final TileEntity tile = world.getTileEntity(message.getPos());

                if (tile instanceof ITileRedstoneControl) {
                    ((ITileRedstoneControl) tile).setRedstoneControl(message.redstoneControl);
                }
            }, world);

            return null;
        }
    }
}
