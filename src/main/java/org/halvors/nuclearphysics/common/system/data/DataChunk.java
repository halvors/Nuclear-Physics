package org.halvors.nuclearphysics.common.system.data;

import com.google.common.primitives.Ints;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants.NBT;
import org.halvors.nuclearphysics.common.NuclearPhysics;

import java.util.ArrayList;
import java.util.List;

public class DataChunk {
    private static final int CHUNK_HEIGHT = 256;
    private static final String NBT_Y_START = "y_start";
    private static final String NBT_SIZE = "size";
    private static final String NBT_LAYERS = "layers";
    private static final String NBT_LAYER_INDEX = "index";
    private static final String NBT_LAYER_Y = "y";
    private static final String NBT_LAYER_DATA = "data";

    private final List<DataLayer> layers = new ArrayList<>();
    private final IBlockAccess world;
    private final ChunkPos pos;

    /** Starting point of the layer array as a Y level */
    private int yStart;

    /** Triggers thread to rescan chunk to calculate exposure values */
    private boolean hasChanged = true;

    public DataChunk(final IBlockAccess world, final ChunkPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public DataChunk(final Chunk chunk) {
        this(chunk.getWorld(), chunk.getChunkCoordIntPair());
    }

    /**
     * Converts y level to layer getIndex
     *
     * @param y
     * @return
     */
    protected int getIndex(int y) {
        return y - yStart;
    }

    /**
     * End point of the layers, inclusive
     *
     * @return
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
    public boolean hasLayer(int y) {
        return y >= yStart && y <= getLayerEnd() && layers.get(getIndex(y)) != null;
    }

    public DataLayer getLayer(int y) {
        // Init array if not initialized
        if (layers.isEmpty()) {
            yStart = Math.max(0, y - 10);
        } else if (y < yStart) { // Check if we need to increase layer array to fit a new value
            // Increase array size by 5 more than expected y level, if under 10 fully expand to zero
            int increase = yStart > 10 ? ((yStart - y) + 5) : yStart;

            //Set new y start
            yStart = yStart - increase;
        }

        // If layer is null, create layer
        if (layers.get(getIndex(y)) == null) {
            layers.add(getIndex(y), new DataLayer(y));
        }

        return layers.get(getIndex(y));
    }

    public void removeLayer(final int y) {
        layers.remove(getIndex(y));
    }

    /**
     * Gets the data from the chunk
     *
     * @param cx - location (0-15)
     * @param y  - location (0-255)
     * @param cz - location (0-15)
     *
     * @return value stored
     */
    public int getValue(int cx, int y, int cz) {
        if (y >= 0 && y < CHUNK_HEIGHT && hasLayer(y)) {
            return getLayer(y).getData(cx, cz);
        }

        return 0;
    }

    /**
     * Sets the value into the chunk
     *
     * @param x     - Chunk x position (0-15)
     * @param y     - Block y location (0-255)
     * @param z     - Chunk z position (0-15)
     * @param value - value to set
     */
    public boolean setValue(final int x, final int y, final int z, final int value) {
        //Keep inside of chunk
        if (y >= 0 && y < CHUNK_HEIGHT) {
            //Only set values that are above zero or have an existing layer
            if (value > 0 || hasLayer(y)) {
                final int prev = getLayer(y).getData(x, z);

                //Set data into layer
                final boolean success = getLayer(y).setData(x, z, value);

                //Remove layer if empty to save memory
                if (getLayer(y).isEmpty()) {
                    removeLayer(y);
                }

                //Check for change
                if (prev != getLayer(y).getData(x, z)) {
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

        for (final DataLayer layer : layers) {
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
        final NBTTagList list = tag.getTagList(NBT_LAYERS, NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            final NBTTagCompound save = list.getCompoundTagAt(i);

            // Load indexs
            int index = save.getInteger(NBT_LAYER_INDEX);
            int y = save.getInteger(NBT_LAYER_Y);

            final DataLayer layer = new DataLayer(y);
            layer.setData(Ints.asList(save.getIntArray(NBT_LAYER_DATA)));

            // Insert layer
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
