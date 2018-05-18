package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;
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

	public PacketTileEntity(final BlockPos pos, final List<Object> objects) {
		super(pos);

		this.objects = objects;
	}

	public <T extends TileEntity & ITileNetwork> PacketTileEntity(T tile) {
		this(new BlockPos(tile), tile.getPacketData(new ArrayList<>()));
	}

	@Override
	public void fromBytes(final ByteBuf dataStream) {
		super.fromBytes(dataStream);

		storedBuffer = dataStream.copy();
	}

	@Override
	public void toBytes(final ByteBuf dataStream) {
		super.toBytes(dataStream);

		PacketHandler.writeObjects(objects, dataStream);
	}

	public static class PacketTileEntityMessage implements IMessageHandler<PacketTileEntity, IMessage> {
		@Override
		public IMessage onMessage(final PacketTileEntity message, final MessageContext messageContext) {
			final World world = PacketHandler.getWorld(messageContext);

			if (world != null) {
				final TileEntity tile = message.getPos().getTileEntity(world);

				if (tile instanceof ITileNetwork) {
					final ITileNetwork tileNetwork = (ITileNetwork) tile;

					try {
						tileNetwork.handlePacketData(message.storedBuffer);
					} catch (Exception e) {
						e.printStackTrace();
					}

					message.storedBuffer.release();
				}
			}

			return null;
		}
	}
}