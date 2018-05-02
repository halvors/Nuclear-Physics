package org.halvors.nuclearphysics.common.utility;

import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.common.tile.ITileRedstoneControl;

public class RedstoneUtility {
    /**
     * Whether or not a certain TileEntity can function with redstone logic. Illogical to use unless the defined TileEntity implements
     * ITileRedstoneControl.
     * @param tile - TileEntity to check
     * @return if the TileEntity can function with redstone logic
     */
    public static boolean canFunction(final TileEntity tile) {
        if (tile instanceof ITileRedstoneControl) {
            final ITileRedstoneControl tileRedstoneControl = (ITileRedstoneControl) tile;

            switch (tileRedstoneControl.getRedstoneControl()) {
                case HIGH:
                    return tileRedstoneControl.isPowered();

                case LOW:
                    return !tileRedstoneControl.isPowered();

                case PULSE:
                    return tileRedstoneControl.isPowered() && !tileRedstoneControl.wasPowered();
            }
        }

        return true;
    }
}
