package org.halvors.quantum.common.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.type.Pair;

import java.util.HashMap;

public class SchematicFusionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fusionReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Block, Integer>> getStructure(ForgeDirection direction, int size) {
        HashMap<Vector3, Pair<Block, Integer>> map = new HashMap<>();

        // Fusion Torus.
        int radius = size + 2;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= size; y++) {
                    Vector3 position = new Vector3(x, y, z);
                    double magnitude = Math.sqrt(x * x + z * z);

                    if (!map.containsKey(position)) {
                        map.put(position, new Pair<>(Blocks.air, 0));
                    }

                    if (magnitude <= radius) {
                        if (y == 0 || y == size) {
                            if (magnitude >= 1) {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * Math.PI) * size / 2;
                                Vector3 newPos = position.clone().translate(0, yDeviation, 0);

                                map.put(newPos.round(), new Pair<>(Quantum.blockElectromagnet, 1));
                            }
                        } else if (magnitude > radius - 1) {
                            map.put(position, new Pair<>(Quantum.blockElectromagnet, 0));
                        }
                    }
                }
            }
        }

        // Fusion Core
        for (int y = 0; y < size; y++) {
            map.put(new Vector3(0, y, 0), new Pair<>(Quantum.blockReactorCell, 0));
            map.put(new Vector3(1, y, 0), new Pair<>(Quantum.blockElectromagnet, 0));
            map.put(new Vector3(0, y, 1), new Pair<>(Quantum.blockElectromagnet, 0));
            map.put(new Vector3(0, y, -1), new Pair<>(Quantum.blockElectromagnet, 0));
            map.put(new Vector3(-1, y, 0), new Pair<>(Quantum.blockElectromagnet, 0));
        }

        map.put(new Vector3(0, 0, 0), new Pair<>(Quantum.blockReactorCell, 0));

        return map;
    }
}
