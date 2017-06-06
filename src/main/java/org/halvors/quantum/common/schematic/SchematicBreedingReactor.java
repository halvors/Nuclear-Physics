package org.halvors.quantum.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.schematic.ISchematic;
import org.halvors.quantum.lib.type.Pair;

import java.util.HashMap;

public class SchematicBreedingReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.breedingReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Block, Integer>> getStructure(ForgeDirection direction, int size) {
        HashMap<Vector3, Pair<Block, Integer>> map = new HashMap<>();

        int radius = Math.max(size, 2);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                map.put(new Vector3(x, 0, z), new Pair<>(Blocks.water, 0));
            }
        }

        radius--;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Vector3 position = new Vector3(x, 0, z);

                if (position.getMagnitude() <= 2) {
                    if (!((x == -radius || x == radius) && (z == -radius || z == radius))) {
                        map.put(new Vector3(x, 0, z), new Pair<Block, Integer>(Quantum.blockReactorCell.block, 0));
                        //map.put(new Vector3(x, -1, z), new Pair<>(Quantum.blockThermometer, 0));
                        //map.put(new Vector3(x, -3, z), new Pair<>(Quantum.blockSiren, 0));
                        map.put(new Vector3(x, -2, z), new Pair<Block, Integer>(Blocks.redstone_wire, 0));
                    } else {
                        map.put(new Vector3(x, -1, z), new Pair<>(Quantum.blockControlRod, 0));
                        map.put(new Vector3(x, -2, z), new Pair<Block, Integer>(Blocks.sticky_piston, 1));
                    }
                }
            }
        }

        map.put(new Vector3(0, -2, 0), new Pair<>(Blocks.stone, 0));
        map.put(new Vector3(0, -3, 0), new Pair<>(Blocks.stone, 0));
        map.put(new Vector3(), new Pair<Block, Integer>(Quantum.blockReactorCell.block, 0));

        return map;
    }
}