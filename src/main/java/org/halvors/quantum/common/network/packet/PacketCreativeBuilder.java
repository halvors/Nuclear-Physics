package org.halvors.quantum.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.HashMap;
import java.util.Map.Entry;

public class PacketCreativeBuilder extends PacketLocation implements IMessage {
    public int schematicId;
    public int size;

    public PacketCreativeBuilder() {

    }

    public PacketCreativeBuilder(BlockPos pos, int schematicId, int size) {
        super(pos);

        this.schematicId = schematicId;
        this.size = size;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        super.fromBytes(dataStream);

        schematicId = dataStream.readInt();
        size = dataStream.readInt();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(schematicId);
        dataStream.writeInt(size);
    }

    public static class PacketCreativeBuilderMessage implements IMessageHandler<PacketCreativeBuilder, IMessage> {
        @Override
        public IMessage onMessage(PacketCreativeBuilder message, MessageContext messageContext) {
            EntityPlayer player = PacketHandler.getPlayer(messageContext);

            if (player != null) {
                PacketHandler.handlePacket(() -> {
                    World world = PacketHandler.getWorld(messageContext);
                    BlockPos pos = message.getPos();

                    if (!world.isRemote) {// && PlayerUtility.isOp(player)) {
                        try {
                            Vector3 position = new Vector3(pos.getX(), pos.getY(), pos.getZ());

                            if (message.size > 0) {
                                // TODO: Implement dynamic facing, not just NORTH.
                                HashMap<Vector3, IBlockState> map = BlockCreativeBuilder.getSchematic(message.schematicId).getStructure(EnumFacing.NORTH, message.size);

                                for (Entry<Vector3, IBlockState> entry : map.entrySet()) {
                                    Vector3 placePos = entry.getKey().clone();
                                    placePos.translate(position);

                                    world.setBlockState(new BlockPos(placePos.getX(), placePos.getY(), placePos.getZ()), entry.getValue());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, player);
            }

            return null;
        }
    }
}
