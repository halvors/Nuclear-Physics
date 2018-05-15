package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Implement this to enable your tile to send and receive data.
 *
 * @author halvors
 */
public interface ITileNetwork {
	/**
	 * Receive and manage a packet's data.
	 * @param dataStream the dataStream to read data from.
	 */
	void handlePacketData(final ByteBuf dataStream);

	/**
	 * Gets a list of data this tile entity keeps synchronized with the client.
	 * @param objects - list of objects
	 * @return List<Object>
	 */
	List<Object> getPacketData(final List<Object> objects);
}

