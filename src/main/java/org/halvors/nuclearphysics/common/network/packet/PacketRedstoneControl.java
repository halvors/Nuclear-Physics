package org.halvors.nuclearphysics.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;
import org.halvors.nuclearphysics.common.utility.type.RedstoneControl;

public class PacketRedstoneControl extends PacketLocation implements IMessage {
    public RedstoneControl redstoneControl;

    public PacketRedstoneControl() {

    }

    public PacketRedstoneControl(BlockPos pos, RedstoneControl redstoneControl) {
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
            EntityPlayer player = PacketHandler.getPlayer(messageContext);

            PacketHandler.handlePacket(() -> {
                World world = PacketHandler.getWorld(messageContext);
                TileEntity tile = world.getTileEntity(message.getPos());

                if (tile instanceof ITileRedstoneControl) {
                    ((ITileRedstoneControl) tile).setRedstoneControl(message.redstoneControl);
                }
            }, player);

            return null;
        }
    }
}
