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
import org.halvors.nuclearphysics.common.tile.ITileNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * This packet i used by tile tile entities to send custom information from server to client.
 */
public class PacketTileEntity extends PacketLocation implements IMessage {
	private List<Object> objects;
	private ByteBuf storedBuffer = null;

	public PacketTileEntity() {

	}

	public PacketTileEntity(BlockPos pos, List<Object> objects) {
		super(pos);

		this.objects = objects;
	}

	public <T extends TileEntity & ITileNetwork> PacketTileEntity(T tile) {
		this(tile.getPos(), tile.getPacketData(new ArrayList<>()));
	}

	@Override
	public void fromBytes(ByteBuf dataStream) {
		super.fromBytes(dataStream);

		storedBuffer = dataStream.copy();
	}

	@Override
	public void toBytes(ByteBuf dataStream) {
		super.toBytes(dataStream);

		PacketHandler.writeObjects(objects, dataStream);
	}

	public static class PacketTileEntityMessage implements IMessageHandler<PacketTileEntity, IMessage> {
		@Override
		public IMessage onMessage(PacketTileEntity message, MessageContext messageContext) {
			final World world = PacketHandler.getWorld(messageContext);

			if (world != null) {
				NuclearPhysics.getProxy().addScheduledTask(() -> {
                    final TileEntity tile = world.getTileEntity(message.getPos());

                    if (tile instanceof ITileNetwork) {
                        final ITileNetwork tileNetwork = (ITileNetwork) tile;

                        try {
                            tileNetwork.handlePacketData(message.storedBuffer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        message.storedBuffer.release();
                    }


                }, world);
			}

			return null;
		}
	}
}