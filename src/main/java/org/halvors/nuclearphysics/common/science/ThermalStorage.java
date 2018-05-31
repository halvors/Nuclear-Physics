package org.halvors.nuclearphysics.common.science;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkDataMap;
import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkStorage;

public class ThermalStorage extends ChunkStorage {
    public ThermalStorage() {
        super("thermal");
    }

    /**
     * Gets the temperature from a block.
     *
     * @param world - world
     * @param pos   - block position
     *
     * @return radioactive material amount
     */
    public static int getTemperature(final IBlockAccess world, final BlockPos pos) {
        final ChunkDataMap map = getMap(world, false);

        if (map != null) {
            return map.getData(pos);
        }

        return 0;
    }

    /**
     * Sets the temperature of a block.
     *
     * @param world       - world
     * @param pos         - block pos
     * @param temperature - temperature
     *
     * @return true if the value was set
     */
    public static void setTemperature(final IBlockAccess world, final BlockPos pos, final int temperature) {
        final ChunkDataMap map = getMap(world, temperature > 0);

        if (map != null) {
            map.setData(pos, temperature);
        }
    }
}
