package org.halvors.quantum.common.base.tile;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Implement this to enable your TileEntity to send and receive data.
 *
 * @author halvors
 */
public interface ITileNetworkable {
	/**
	 * Receive and manage a packet's data.
	 * @param dataStream the dataStream to read data from.
	 */
	void handlePacketData(ByteBuf dataStream) throws Exception;

	/**
	 * Gets a list of data this tile entity keeps synchronized with the client.
	 * @param objects - list of objects
	 * @return List<Object>
	 */
	List<Object> getPacketData(List<Object> objects);
}

