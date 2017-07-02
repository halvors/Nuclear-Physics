package org.halvors.quantum.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.type.Pair;

import java.util.HashMap;

public class SchematicBreedingReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.breedingReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Block, Integer>> getStructure(EnumFacing direction, int size) {
        HashMap<Vector3, Pair<Block, Integer>> map = new HashMap<>();

        int radius = Math.max(size, 2);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                map.put(new Vector3(x, 0, z), new Pair<>(Blocks.WATER, 0));
            }
        }

        radius--;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Vector3 position = new Vector3(x, 0, z);

                if (position.getMagnitude() <= 2) {
                    if (!((x == -radius || x == radius) && (z == -radius || z == radius))) {
                        map.put(new Vector3(x, 0, z), new Pair<>(QuantumBlocks.blockReactorCell, 0));
                        map.put(new Vector3(x, -1, z), new Pair<>(QuantumBlocks.blockThermometer, 0));
                        map.put(new Vector3(x, -3, z), new Pair<>(QuantumBlocks.blockSiren, 0));
                        map.put(new Vector3(x, -2, z), new Pair<>(Blocks.REDSTONE_WIRE, 0));
                    } else {
                        map.put(new Vector3(x, -1, z), new Pair<>(QuantumBlocks.blockControlRod, 0));
                        map.put(new Vector3(x, -2, z), new Pair<>(Blocks.STICKY_PISTON, 1));
                    }
                }
            }
        }

        map.put(new Vector3(0, -2, 0), new Pair<>(Blocks.STONE, 0));
        map.put(new Vector3(0, -3, 0), new Pair<>(Blocks.STONE, 0));
        map.put(new Vector3(), new Pair<>(QuantumBlocks.blockReactorCell, 0));

        return map;
    }
}