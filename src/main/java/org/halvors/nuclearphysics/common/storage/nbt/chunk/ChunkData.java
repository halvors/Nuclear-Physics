package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants;

public class ChunkData {
    private static final int CHUNK_HEIGHT = 256;
    private static final String NBT_Y_START = "y_start";
    private static final String NBT_SIZE = "size";
    private static final String NBT_LAYERS = "layers";
    private static final String NBT_LAYER_INDEX = "index";
    private static final String NBT_LAYER_Y = "y";
    private static final String NBT_LAYER_DATA = "data";

    private final IBlockAccess world;
    private final int x;
    private final int z;

    /** Array of active layers, modified by yStart */
    private ChunkDataLayer[] layers = new ChunkDataLayer[CHUNK_HEIGHT];

    /** Starting point of the layer array as a Y level */
    private int yStart;

    /** Triggers thread to rescan chunk to calculate exposure values */
    public boolean hasChanged = true;

    public ChunkData(IBlockAccess world, ChunkPos chunkPos) {
        this.world = world;
        this.x = chunkPos.chunkXPos;
        this.z = chunkPos.chunkZPos;
    }

    /**
     * Gets the data from the chunk
     *
     * @param cx - location (0-15)
     * @param y  - location (0-255)
     * @param cz - location (0-15)
     * @return value stored
     */
    public int getValue(int cx, int y, int cz) {
        if (y >= 0 && y < CHUNK_HEIGHT && hasLayer(y)) {
            final ChunkDataLayer layer = getLayer(y);

            return layer.getData(cx, cz);
        }

        return 0;
    }

    /**
     * Sets the value into the chunk
     *
     * @param cx    - location (0-15)
     * @param y     - location (0-255)
     * @param cz    - location (0-15)
     * @param value - value to set
     */
    public boolean setValue(int cx, int y, int cz, int value) {
        // Keep inside of chunk
        if (y >= 0 && y < CHUNK_HEIGHT) {
            // Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y)) {
                final ChunkDataLayer layer = getLayer(y);
                final int prev = layer.getData(cx, cz);

                // Set data into layer
                final boolean b = layer.setData(cx, cz, value);

                // Remove layer if empty to save memory
                if (layer.isEmpty()) {
                    removeLayer(y);
                }

                // Check for change
                if (prev != layer.getData(cx, cz)) {
                    hasChanged = true;
                }

                return b;
            }

            return true; // value was zero with no layer, return true as in theory prev = 0 and value = 0
        }

        return false;
    }

    public int getIndex(int y) {
        return y - yStart;
    }

    /**
     * Checks if there is a layer for the y level
     *
     * @param y - y level (0-255)
     * @return true if layer exists
     */
    public boolean hasLayer(int y) {
        return layers != null && y >= yStart && y <= getLayerEnd() && layers[getIndex(y)] != null;
    }

    public ChunkDataLayer getLayer(int y) {
        // If layer is null, create layer
        if (layers[getIndex(y)] == null) {
            layers[getIndex(y)] = new ChunkDataLayer(y);
        }

        return layers[getIndex(y)];
    }

    public int getLayerEnd() {
        return yStart + layers.length - 1;
    }

    public void removeLayer(int y) {
        final int index = getIndex(y);

        if (index >= 0 && index < layers.length) {
            layers[index] = null;
        }
    }

    public void readFromNBT(final NBTTagCompound tag) {
        // Set y start
        yStart = tag.getInteger(NBT_Y_START);

        // Rebuild array
        final int size = tag.getInteger(NBT_SIZE);
        layers = new ChunkDataLayer[size];

        // Load layers
        final NBTTagList list = tag.getTagList(NBT_LAYERS, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            final NBTTagCompound dataTag = list.getCompoundTagAt(i);

            // Load indexs
            final int index = dataTag.getInteger(NBT_LAYER_INDEX);
            final int y = dataTag.getInteger(NBT_LAYER_Y);

            // Create layer
            final ChunkDataLayer layer = new ChunkDataLayer(y);

            // Load data
            final int[] data = dataTag.getIntArray(NBT_LAYER_DATA);

            // Copy over array
            for (int j = 0; j < data.length && j < layer.getData().length; j++) {
                layer.getData()[j] = data[j];

                if (data[j] != 0) {
                    layer.blocksUsed++;
                }
            }

            // Insert layer
            layers[index] = layer;
        }
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        if (layers != null) {
            tag.setInteger(NBT_Y_START, yStart);
            tag.setInteger(NBT_SIZE, layers.length);

            final NBTTagList list = new NBTTagList();

            for (int i = 0; i < layers.length; i++) {
                ChunkDataLayer layer = layers[i];

                if (layer != null && !layer.isEmpty()) {
                    final NBTTagCompound dataTag = new NBTTagCompound();
                    dataTag.setInteger(NBT_LAYER_INDEX, i);
                    dataTag.setInteger(NBT_LAYER_Y, layer.getY());
                    dataTag.setIntArray(NBT_LAYER_DATA, layer.getData());
                    list.appendTag(dataTag);
                }
            }

            tag.setTag(NBT_LAYERS, list);
        }

        return tag;
    }
}
