package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import java.util.HashMap;
import java.util.Map.Entry;

public class PacketCreativeBuilder extends PacketLocation implements IMessage {
    public int schematicId;
    public int size;

    public PacketCreativeBuilder() {

    }

    public PacketCreativeBuilder(final BlockPos pos, final int schematicId, final int size) {
        super(pos);

        this.schematicId = schematicId;
        this.size = size;
    }

    @Override
    public void fromBytes(final ByteBuf dataStream) {
        super.fromBytes(dataStream);

        schematicId = dataStream.readInt();
        size = dataStream.readInt();
    }

    @Override
    public void toBytes(final ByteBuf dataStream) {
        super.toBytes(dataStream);

        dataStream.writeInt(schematicId);
        dataStream.writeInt(size);
    }

    public static class PacketCreativeBuilderMessage implements IMessageHandler<PacketCreativeBuilder, IMessage> {
        @Override
        public IMessage onMessage(final PacketCreativeBuilder message, final MessageContext messageContext) {
            final World world = PacketHandler.getWorld(messageContext);
            final EntityPlayer player = PacketHandler.getPlayer(messageContext);
            final BlockPos pos = message.getPos();

            if (!world.isRemote && PlayerUtility.isOp(player)) {
                try {
                    if (message.size > 0) {
                        // TODO: Implement dynamic facing, not just NORTH.
                        final HashMap<BlockPos, Pair<Block, Integer>> map = BlockCreativeBuilder.getSchematic(message.schematicId).getStructure(ForgeDirection.NORTH, message.size);

                        for (final Entry<BlockPos, Pair<Block, Integer>> entry : map.entrySet()) {
                            final BlockPos placePos = entry.getKey().add(pos);

                            placePos.setBlock(entry.getValue().getLeft(), entry.getValue().getRight(), 2, world);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}
