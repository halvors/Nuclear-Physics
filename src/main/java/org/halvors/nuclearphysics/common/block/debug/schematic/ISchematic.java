package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

/*
 * Stores building structure data.
 */
public interface ISchematic {
    /** The name of the schematic that is unlocalized.
     *
     * @return "schematic.NAME-OF-SCHEMATIC.name" */
    String getName();

    /** Gets the structure of the schematic.
     *
     * @param size - The size multiplier.
     * @return A Hashmap of positions and block IDs with metadata. */
    HashMap<BlockPos, IBlockState> getStructure(EnumFacing direction, int size);
}
