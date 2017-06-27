package org.halvors.quantum.common.schematic;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
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
    HashMap<Vector3, Pair<Block, Integer>> getStructure(ForgeDirection direction, int size);
}
