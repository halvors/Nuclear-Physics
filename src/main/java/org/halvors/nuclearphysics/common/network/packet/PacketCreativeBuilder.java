package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import java.util.HashMap;
import java.util.Map.Entry;

public class PacketCreativeBuilder extends PacketLocation implements IMessage {
    public int schematicId;
    public int size;

    public PacketCreativeBuilder() {

    }

    public PacketCreativeBuilder(Position pos, int schematicId, int size) {
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
                World world = PacketHandler.getWorld(messageContext);
                int x = message.getX();
                int y = message.getY();
                int z = message.getZ();

                if (!world.isRemote && PlayerUtility.isOp(player)) {
                    try {
                        if (message.size > 0) {
                            // TODO: Implement dynamic facing, not just NORTH.
                            HashMap<Position, Pair<Block, Integer>> map = BlockCreativeBuilder.getSchematic(message.schematicId).getStructure(ForgeDirection.NORTH, message.size);

                            for (Entry<Position, Pair<Block, Integer>> entry : map.entrySet()) {
                                Position placePos = entry.getKey().add(x, y, z);

                                world.setBlock(placePos.getIntX(), placePos.getIntY(), placePos.getIntZ(), entry.getValue().getLeft(), entry.getValue().getRight(), 2);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }
    }
}
