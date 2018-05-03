package org.halvors.nuclearphysics.common.system.chunk;

import com.google.common.primitives.Ints;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import org.halvors.nuclearphysics.common.NuclearPhysics;

import java.util.ArrayList;
import java.util.List;

public class ChunkData {
    public static final int CHUNK_HEIGHT = 256;
    private static final String NBT_Y_START = "y_start";
    private static final String NBT_SIZE = "size";
    private static final String NBT_LAYERS = "layers";
    private static final String NBT_LAYER_INDEX = "index";
    private static final String NBT_LAYER_Y = "y";
    private static final String NBT_LAYER_DATA = "chunk";

    private final List<ChunkDataLayer> layers = new ArrayList<>();
    private final IBlockAccess world;
    private final ChunkPos pos;

    /** Starting point of the layer array as a Y level */
    private int yStart;

    /** Triggers thread to rescan chunk to calculate exposure values */
    private boolean hasChanged = true;

    public ChunkData(final IBlockAccess world, final ChunkPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public ChunkData(final Chunk chunk) {
        this(chunk.getWorld(), chunk.getChunkCoordIntPair());
    }

    /**
     * Converts y level to layer index.
     *
     * @param y - y level (0-255)
     *
     * @return index
     */
    public int getIndex(final int y) {
        return y - yStart;
    }

    /**
     * End point of the layers, inclusive
     *
     * @return layer end
     */
    public int getLayerEnd() {
        return yStart + layers.size() - 1;
    }

    /**
     * Checks if there is a layer for the y level
     *
     * @param y - y level (0-255)
     *
     * @return true if layer exists
     */
    public boolean hasLayer(final int y) {
        return y >= yStart && y <= getLayerEnd() && layers.get(getIndex(y)) != null;
    }

    public ChunkDataLayer getLayer(int y) {
        if (layers.isEmpty()) {
            yStart = Math.max(0, y - 10);
        } else if (y < yStart) { // Check if we need to increase layer array to fit a new value
            // Increase array size by 5 more than expected y level, if under 10 fully expand to zero
            int increase = yStart > 10 ? ((yStart - y) + 5) : yStart;

            // Set new y start
            yStart = yStart - increase;
        }

        final int index = getIndex(y);

        // If layer is null, create layer
        if (layers.get(index) == null) {
            layers.add(index, new ChunkDataLayer(y));
        }

        return layers.get(index);
    }

    public void removeLayer(final int y) {
        layers.remove(getIndex(y));
    }

    /**
     * Gets the chunk from the chunk
     *
     * @param x - Chunk x position (0-15)
     * @param y  - Block y layer (0-255)
     * @param z - Chunk z position (0-15)
     *
     * @return value stored
     */
    public int getValue(final int x, final int y, final int z) {
        if (y >= 0 && y < CHUNK_HEIGHT && hasLayer(y)) {
            return getLayer(y).getData(x, z);
        }

        return 0;
    }

    /**
     * Sets the value into the chunk
     *
     * @param x     - Chunk x position (0-15)
     * @param y     - Block y layer (0-255)
     * @param z     - Chunk z position (0-15)
     * @param value - value to set
     */
    public boolean setValue(final int x, final int y, final int z, final int value) {
        // TODO: Remove debugging message.
        NuclearPhysics.getLogger().info("Setting ChunkData with value: " + value);

        //Keep inside of chunk
        if (y >= 0 && y < CHUNK_HEIGHT) {
            //Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y)) {
                final ChunkDataLayer layer = getLayer(y);
                final int prev = layer.getData(x, z);

                // Set chunk into layer
                final boolean success = layer.setData(x, z, value);

                // Remove layer if empty to save memory
                if (layer.isEmpty()) {
                    removeLayer(y);
                }

                // Check for change
                if (prev != layer.getData(x, z)) {
                    hasChanged = true;
                }

                return success;
            }

            return true; //value was zero with no layer, return true as in theory prev = 0 and value = 0
        } else {
            NuclearPhysics.getLogger().info("Something tried to place a block outside map", new RuntimeException("trace"));
        }

        return false;
    }

    public void writeToNBT(final NBTTagCompound tag) {
        tag.setInteger(NBT_Y_START, yStart);
        tag.setInteger(NBT_SIZE, layers.size());

        final NBTTagList list = new NBTTagList();

        for (final ChunkDataLayer layer : layers) {
            if (layer != null && !layer.isEmpty()) {
                final NBTTagCompound save = new NBTTagCompound();
                save.setInteger(NBT_LAYER_INDEX, layers.indexOf(layer));
                save.setInteger(NBT_LAYER_Y, layer.getY());
                save.setIntArray(NBT_LAYER_DATA, Ints.toArray(layer.getData()));
                list.appendTag(save);
            }
        }

        tag.setTag(NBT_LAYERS, list);
    }

    public void readFromNBT(final NBTTagCompound tag) {
        // Set y start
        yStart = tag.getInteger(NBT_Y_START);

        // Load layers
        final NBTTagList list = tag.getTagList(NBT_LAYERS, Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            final NBTTagCompound save = list.getCompoundTagAt(i);

            // Load indexs
            final int index = save.getInteger(NBT_LAYER_INDEX);
            final int y = save.getInteger(NBT_LAYER_Y);
            final ChunkDataLayer layer = new ChunkDataLayer(y);
            layer.setData(Ints.asList(save.getIntArray(NBT_LAYER_DATA)));

            layers.add(index, layer);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public IBlockAccess getWorld() {
        return world;
    }

    public ChunkPos getPos() {
        return pos;
    }
}
