package org.halvors.nuclearphysics.api.schematic;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;

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
     * @return A Hashmap of positions and block states. */
    HashMap<Position, Pair<Block, Integer>> getStructure(ForgeDirection facing, int size);
}
