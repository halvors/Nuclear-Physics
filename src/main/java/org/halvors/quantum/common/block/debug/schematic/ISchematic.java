package org.halvors.quantum.common.block.debug.schematic;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.type.Pair;

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
    HashMap<Vector3, Pair<Block, Integer>> getStructure(EnumFacing direction, int size);
}
