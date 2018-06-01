package org.halvors.nuclearphysics.common.storage.nbt.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ChunkData {
    private static final int CHUNK_HEIGHT = 256;
    private static final String NBT_Y_START = "y_start";
    private static final String NBT_SIZE = "size";
    private static final String NBT_LAYERS = "layers";
    private static final String NBT_LAYER_INDEX = "index";
    private static final String NBT_LAYER_HEIGHT = "height";
    private static final String NBT_LAYER_DATA = "data";

    /** Array of active layers, modified by yStart */
    private ChunkDataLayer[] layers = new ChunkDataLayer[CHUNK_HEIGHT];

    /** Starting point of the layer array as a Y level */
    private int yStart;

    public ChunkData() {

    }

    /**
     * Gets the data from the data
     *
     * @param x - location (0-15)
     * @param y - location (0-255)
     * @param z - location (0-15)
     * @return value stored
     */
    public int getValue(int x, int y, int z) {
        if (y >= 0 && y < CHUNK_HEIGHT && hasLayer(y)) {
            final ChunkDataLayer layer = getLayer(y);

            return layer.getValue(x, z);
        }

        return 0;
    }

    /**
     * Sets the value into the data
     *
     * @param x    - location (0-15)
     * @param y    - location (0-255)
     * @param z    - location (0-15)
     * @param value - value to set
     */
    public boolean setValue(int x, int y, int z, int value) {
        boolean changed = false;

        if (y >= 0 && y < CHUNK_HEIGHT) {
            // Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y)) {
                final ChunkDataLayer layer = getLayer(y);

                // Set data into layer
                changed = layer.setValue(x, z, value);

                // Remove layer if empty to save memory
                if (layer.isEmpty()) {
                    removeLayer(y);
                }
            }
        }

        return changed;
    }

    public int getIndex(int y) {
        return y - yStart;
    }

    public boolean hasLayer(int y) {
        return layers != null && y >= yStart && y <= getLayerEnd() && layers[getIndex(y)] != null;
    }

    public ChunkDataLayer getLayer(int y) {
        final int index = getIndex(y);

        // If layer is null, create layer
        if (layers[index] == null) {
            layers[index] = new ChunkDataLayer(y);
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
            final int y = dataTag.getInteger(NBT_LAYER_HEIGHT);

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
                final ChunkDataLayer layer = layers[i];

                if (layer != null && !layer.isEmpty()) {
                    final NBTTagCompound dataTag = new NBTTagCompound();
                    dataTag.setInteger(NBT_LAYER_INDEX, i);
                    dataTag.setInteger(NBT_LAYER_HEIGHT, layer.getY());
                    dataTag.setIntArray(NBT_LAYER_DATA, layer.getData());
                    list.appendTag(dataTag);
                }
            }

            tag.setTag(NBT_LAYERS, list);
        }

        return tag;
    }
}