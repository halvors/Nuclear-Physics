package org.halvors.quantum.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.type.Pair;

import java.util.HashMap;

public class SchematicAccelerator implements ISchematic {
    @Override
    public String getName() {
        return "schematic.accelerator.name";
    }

    @Override
    public HashMap<Vector3, Pair<Block, Integer>> getStructure(EnumFacing direction, int size) {
        HashMap<Vector3, Pair<Block, Integer>> map = new HashMap<>();
        int radius = size;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new Vector3(x, y, z), new Pair<>(Quantum.blockElectromagnet, 0));
                    }
                }
            }
        }

        radius = size - 2;

        for (int x = -radius; x < radius; x++)
        {
            for (int z = -radius; z < radius; z++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1)
                    {
                        map.put(new Vector3(x, y, z), new Pair<>(Quantum.blockElectromagnet, 0));
                    }
                }
            }
        }

        radius = size - 1;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        if (y == -1 || y == 1) {
                            map.put(new Vector3(x, y, z), new Pair<>(Quantum.blockElectromagnet, 1));
                        } else if (y == 0) {
                            map.put(new Vector3(x, y, z), new Pair<>(Blocks.AIR, 0));
                        }
                    }
                }
            }
        }

        /*
        //Bottom
        map.putAll(getBox(new Vector3(0, 0, 0), Quantum.blockElectromagnet.block, 1, size));
        map.putAll(getBox(new Vector3(0, 0, 0), Quantum.blockElectromagnet.block, 0, size - 1));
        map.putAll(getBox(new Vector3(0, 0, 0), Quantum.blockElectromagnet.block, 0, size + 1));

        //Mid
        map.putAll(getBox(new Vector3(0, 1, 0), Blocks.air, 0, size));
        map.putAll(getBox(new Vector3(0, 1, 0), Quantum.blockElectromagnet.block, 1, size - 1));
        map.putAll(getBox(new Vector3(0, 1, 0), Quantum.blockElectromagnet.block, 1, size + 1));

        //Top
        map.putAll(getBox(new Vector3(0, 2, 0), Quantum.blockElectromagnet.block, 1, size));
        map.putAll(getBox(new Vector3(0, 2, 0), Quantum.blockElectromagnet.block, 0, size - 1));
        map.putAll(getBox(new Vector3(0, 2, 0), Quantum.blockElectromagnet.block, 0, size + 1));
        */

        return map;
    }
}