package org.halvors.nuclearphysics.common.system;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.system.chunk.ChunkDataMap;

import java.util.HashMap;

public class ThermalSystem {
    /** Temperature map, saved to world and updated over time. */
    private static final HashMap<IBlockAccess, ChunkDataMap> worldToTemperatureMap = new HashMap<>();

    public ThermalSystem() {

    }

    /**
     * Gets the map of radioactive material per positon. Is used
     * to calculate exposure and other effects.
     *
     * @param world  - world of the map to get
     * @param init - true to generate the map
     * @return map, or null if it was never created
     */
    public static ChunkDataMap getTemperatureMap(final IBlockAccess world, final boolean init) {
        return ChunkDataMap.getMap(worldToTemperatureMap, world, init);
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
        final ChunkDataMap map = getTemperatureMap(world, false);

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
        final ChunkDataMap map = getTemperatureMap(world, temperature > 0);

        if (map != null) {
            map.setData(pos, temperature);
        }
    }
}
