package org.halvors.nuclearphysics.common.system.chunk;

/**
 * Single Y level of radiation stores in the world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/24/2018.
 */
public class ChunkDataLayer {
    private static final int CHUNK_WIDTH = 16;

    /** Index of this layer */
    private final int y;

    /** Stored data in this layer */
    private final int[] data = new int[CHUNK_WIDTH * CHUNK_WIDTH];

    /** Number of non-zero slots, used to track if layer is empty */
    public int blocksUsed = 0;

    public ChunkDataLayer(int y) {
        this.y = y;
    }

    /**
     * Gets the data from the layer
     *
     * @param x - location
     * @param z - location
     * @return value
     */
    public int getData(int x, int z) {
        int index = index(x, z);

        if (index >= 0) {
            return data[index];
        }

        return 0;
    }

    /**
     * Sets data into the layer
     *
     * @param x     - location
     * @param z     - location
     * @param value - value
     * @return true if data was set, false if nothing happened (likely means outside of the map)
     */
    public boolean setData(int x, int z, int value) {
        int index = index(x, z);

        if (index >= 0) {
            int prev = data[index];
            data[index] = value;

            if (prev != 0 && value == 0) {
                blocksUsed--;
            } else if (prev == 0 && value != 0) {
                blocksUsed++;
            }

            return true;
        }

        return false;
    }

    /**
     * Index of the x z location
     *
     * @param x - location 0-15
     * @param z - location 0-15
     * @return index between 0-255, -1 returns if input data is invalid
     */
    public final int index(int x, int z) {
        // Bound check to prevent index values from generating outside range
        // Is needed as a negative z can cause a value to overlap values normally in range
        // Ex: 15x -1z -> 239, which is in range but not the right index

        if (x >= 0 && x < CHUNK_WIDTH && z >= 0 && z < CHUNK_WIDTH) {
            return x * CHUNK_WIDTH + z;
        }

        return -1;
    }

    public boolean isEmpty() {
        return blocksUsed <= 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getY() {
        return y;
    }

    public int[] getData() {
        return data;
    }
}
