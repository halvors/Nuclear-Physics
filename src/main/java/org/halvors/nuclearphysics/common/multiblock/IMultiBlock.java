package org.halvors.nuclearphysics.common.multiblock;

import org.halvors.nuclearphysics.api.BlockPos;

/** Interface to be applied to tile entity blocks that occupies more than one block space. Useful for
 * large machines.
 *
 * @author Calclavia */
public interface IMultiBlock {
    /** @return An array of Vector3 containing the multiblock relative coordinates to be constructed. */
    BlockPos[] getMultiBlockVectors();
}