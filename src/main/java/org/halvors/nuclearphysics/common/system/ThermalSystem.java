package org.halvors.nuclearphysics.common.system;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.system.data.DataMap;
import org.halvors.nuclearphysics.common.system.event.WorldEventHandler;

public class ThermalSystem {
    /**
     * Gets the temperature from a block.
     *
     * @param world - world
     * @param pos   - block position
     *
     * @return radioactive material amount
     */
    public static int getTemperature(final IBlockAccess world, final BlockPos pos) {
        final DataMap map = WorldEventHandler.getTemperatureMap(world, false);

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
    public static boolean setTemperature(final IBlockAccess world, final BlockPos pos, final int temperature) {
        final DataMap map = WorldEventHandler.getTemperatureMap(world, temperature > 0);

        if (map != null) {
            return map.setData(pos, temperature);
        }

        return true;
    }
}
