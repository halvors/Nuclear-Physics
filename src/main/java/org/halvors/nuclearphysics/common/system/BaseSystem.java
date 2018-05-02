package org.halvors.nuclearphysics.common.system;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.system.event.WorldEventHandler;

public class BaseSystem {
    /**
     * Gets the temperature from a block.
     *
     * @param world - world
     * @param pos   - block position
     *
     * @return radioactive material amount
     */
    public int getTemperature(final IBlockAccess world, final BlockPos pos) {
        final BaseMap map = WorldEventHandler.getTemperatureMap(world, false);

        if (map != null) {
            return map.getData(pos);
        }

        return 0;
    }

    /**
     * Sets the temperature of a block.
     *
     * @param world  - world
     * @param pos    - block pos
     * @param amount - temperature
     *
     * @return true if the value was set
     */
    public boolean setTemperature(final IBlockAccess world, final BlockPos pos, final int amount) {
        final BaseMap map = WorldEventHandler.getTemperatureMap(world, amount > 0);

        if (map != null) {
            return map.setData(pos, amount);
        }

        return true;
    }
}
