package org.halvors.nuclearphysics.common.system.chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkDataLayer {
    private static final int CHUNK_WIDTH = 16;

    /** Stored chunk in this layer */
    private final List<Integer> data = new ArrayList<>();

    /** The index of this layer */
    private final int y;

    public ChunkDataLayer(final int y) {
        this.y = y;
    }

    /**
     * Is the layer empty
     *
     * @return true if no blocks were ever set
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * Index of the x z location
     *
     * @param x - location 0-15
     * @param z - location 0-15
     * @return getIndex between 0-255, -1 returns if input chunk is invalid
     */
    public final int getIndex(final int x, final int z) {
        // Bound check to prevent getIndex values from generating outside range
        // Is needed as a negative z can cause a value to overlap values normally in range
        // Ex: 15x -1z -> 239, which is in range but not the right getIndex
        if (x >= 0 && x < CHUNK_WIDTH && z >= 0 && z < CHUNK_WIDTH) {
            return x * CHUNK_WIDTH + z;
        }

        return -1;
    }

    /**
     * Gets the chunk from the layer
     *
     * @param x - location
     * @param z - location
     * @return value
     */
    public int getData(final int x, final int z) {
        final int index = getIndex(x, z);

        if (index >= 0) {
            return data.get(index);
        }

        return 0;
    }

    /**
     * Sets chunk into the layer
     *
     * @param x     - location
     * @param z     - location
     * @param value - value
     * @return true if chunk was set, false if nothing happened (likely means outside of the map)
     */
    public boolean setData(final int x, final int z, final int value) {
        final int index = getIndex(x, z);

        if (index >= 0) {
            data.add(index, value);

            return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Integer> getData() {
        return data;
    }

    public void setData(final List<Integer> list) {
        data.clear();
        data.addAll(list);
    }

    public int getY() {
        return y;
    }
}
