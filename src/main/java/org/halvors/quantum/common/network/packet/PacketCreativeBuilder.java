package org.halvors.quantum.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.utility.location.Location;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class PacketCreativeBuilder extends PacketLocation implements IMessage {
    public int schematicId;
    public int size;

    public PacketCreativeBuilder() {

    }

    public PacketCreativeBuilder(Location location, int schematicId, int size) {
        super(location);

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
            Location location = message.getLocation();
            World world = PacketHandler.getWorld(messageContext);

            if (!world.isRemote) {
                // TODO: Only allow operators.

                try {
                    Vector3 position = new Vector3(location.getX(), location.getY(), location.getZ());

                    if (message.size > 0) {
                        // TODO: EnumFacing.fromAngle() correct replacement for ForgeDirection.getOrientation()?
                        /*HashMap<Vector3, Pair<Block, Integer>> map = BlockCreativeBuilder.getSchematic(message.schematicId).getStructure(EnumFacing.fromAngle(position.getBlock(world)), message.size);

                        for (Map.Entry<Vector3, Pair<Block, Integer>> entry : map.entrySet()) {
                            Vector3 placePos = entry.getKey().clone();
                            placePos.translate(position);
                            placePos.setBlock(world, entry.getValue().getLeft(), entry.getValue().getRight());
                        }
                        */
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
