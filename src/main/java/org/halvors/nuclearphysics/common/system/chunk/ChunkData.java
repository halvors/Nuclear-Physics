package org.halvors.nuclearphysics.common.system.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants;
import org.halvors.nuclearphysics.common.NuclearPhysics;

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
    private ChunkDataLayer[] layers;

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
     * Sets the value into the chunk
     *
     * @param cx    - location (0-15)
     * @param y     - location (0-255)
     * @param cz    - location (0-15)
     * @param value - value to set
     */
    public boolean setValue(int cx, int y, int cz, int value) {
        //Keep inside of chunk
        if (y >= 0 && y < CHUNK_HEIGHT) {
            //Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y)) {
                int prev = getLayer(y).getData(cx, cz);

                //Set data into layer
                boolean b = getLayer(y).setData(cx, cz, value);

                //Remove layer if empty to save memory
                if (getLayer(y).isEmpty()) {
                    removeLayer(y);
                }

                //Check for change
                if (prev != getLayer(y).getData(cx, cz)) {
                    hasChanged = true;
                }

                //Return
                return b;
            }

            return true; //value was zero with no layer, return true as in theory prev = 0 and value = 0
        } else {
            NuclearPhysics.getLogger().info("Something tried to place a block outside map", new RuntimeException("trace"));
        }

        return false;
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
            return getLayer(y).getData(cx, cz);
        }
        return 0;
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

    public int getLayerEnd() {
        return yStart + layers.length - 1;
    }

    public void removeLayer(int y) {
        int index = getIndex(y);

        if (index >= 0 && index < layers.length) {
            layers[index] = null;
        }
    }

    public ChunkDataLayer getLayer(int y) {
        // Init array if not initialized
        if (layers == null) {
            layers = new ChunkDataLayer[11];
            yStart = Math.max(0, y - 10);
        } else if (y < yStart) { // Check if we need to increase layer array to fit a new value
            final ChunkDataLayer[] oldLayers = layers;

            // Increase array size by 5 more than expected y level, if under 10 fully expand to zero
            int increase = yStart > 10 ? ((yStart - y) + 5) : yStart;

            // New array
            int newLength = Math.min(CHUNK_HEIGHT, oldLayers.length + increase);
            layers = new ChunkDataLayer[newLength];

            // Copy array
            System.arraycopy(oldLayers, 0, layers, increase, oldLayers.length);

            // Set new y start
            yStart = yStart - increase;
        } else if (y > getLayerEnd()) {
            ChunkDataLayer[] oldLayers = layers;

            // Increase array size by 5 above y
            int increase = y - (yStart + layers.length) + 5;
            layers = new ChunkDataLayer[Math.min(CHUNK_HEIGHT, oldLayers.length + increase)];

            // Copy array
            System.arraycopy(oldLayers, 0, layers, 0, oldLayers.length);
        }

        //If layer is null, create layer
        if (layers[getIndex(y)] == null) {
            layers[getIndex(y)] = new ChunkDataLayer(y);
        }

        return layers[getIndex(y)];
    }

    public int getIndex(int y) {
        return y - yStart;
    }

    public void readFromNBT(NBTTagCompound tag) {
        // Set y start
        this.yStart = tag.getInteger(NBT_Y_START);

        // Rebuild array
        int size = tag.getInteger(NBT_SIZE);
        this.layers = new ChunkDataLayer[size];

        // Load layers
        NBTTagList list = tag.getTagList(NBT_LAYERS, Constants.NBT.TAG_COMPOUND);

        for (int list_index = 0; list_index < list.tagCount(); list_index++) {
            NBTTagCompound save = list.getCompoundTagAt(list_index);

            // Load indexs
            int index = save.getInteger(NBT_LAYER_INDEX);
            int y = save.getInteger(NBT_LAYER_Y);

            // Create layer
            ChunkDataLayer layer = new ChunkDataLayer(y);

            // Load data
            int[] data = save.getIntArray(NBT_LAYER_DATA);

            // Error if invalid size (unlikely to happen unless corruption or user errors)
            if (data.length != layer.getData().length) {
                NuclearPhysics.getLogger().info(String.format("RadiationChunk[%sd, %scx, %scz]#load(NBT) layer[%s] -> data array has " +
                                                              "invalid size, will attempt to read in as much as possible. This may result" +
                                                              "in radiation values changing per position for the given y level.", world, x, z, y));
            }

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

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (layers != null) {
            tag.setInteger(NBT_Y_START, yStart);
            tag.setInteger(NBT_SIZE, layers.length);
            NBTTagList list = new NBTTagList();

            for (int i = 0; i < layers.length; i++) {
                ChunkDataLayer layer = layers[i];

                if (layer != null && !layer.isEmpty()) {
                    NBTTagCompound subTag = new NBTTagCompound();
                    subTag.setInteger(NBT_LAYER_INDEX, i);
                    subTag.setInteger(NBT_LAYER_Y, layer.getY());
                    subTag.setIntArray(NBT_LAYER_DATA, layer.getData());
                    list.appendTag(subTag);
                }
            }

            tag.setTag(NBT_LAYERS, list);
        }

        return tag;
    }
}
