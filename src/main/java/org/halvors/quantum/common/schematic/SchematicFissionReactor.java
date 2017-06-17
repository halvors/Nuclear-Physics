package org.halvors.quantum.common.schematic;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.type.Pair;

import java.util.HashMap;

public class SchematicFissionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fissionReactor.name";
    }

    @Override
    public HashMap<Vector3, Pair<Block, Integer>> getStructure(ForgeDirection direction, int size) {
        HashMap<Vector3, Pair<Block, Integer>> map = new HashMap<>();
        int radius = 2;

        if (size <= 1) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Vector3 targetPosition = new Vector3(x, 0, z);
                    map.put(targetPosition, new Pair<>(Blocks.water, 0));
                }
            }

            radius--;

            // Create turbines and control rods.
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Vector3 targetPosition = new Vector3(x, 1, z);
                    map.put(targetPosition, new Pair<>(Quantum.blockElectricTurbine, 0));

                    if (!(x == -radius || x == radius && z == -radius || z == radius) && new Vector3(x, 0, z).getMagnitude() <= 1) {
                        map.put(new Vector3(x, -1, z), new Pair<>(Quantum.blockControlRod, 0));
                        map.put(new Vector3(x, -2, z), new Pair<Block, Integer>(Blocks.sticky_piston, 1));
                    }
                }
            }

            map.put(new Vector3(0, -3, 0), new Pair<>(Quantum.blockSiren, 0));
            map.put(new Vector3(0, -2, 0), new Pair<Block, Integer>(Blocks.redstone_wire, 0));
            map.put(new Vector3(), new Pair<>(Quantum.blockReactorCell, 0));
        } else {
            for (int y = 0; y < size; y++) {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        Vector3 targetPosition = new Vector3(x, y, z);
                        Vector3 leveledPosition = new Vector3(0, y, 0);

                        if (y < size - 1) {
                            if (targetPosition.distance(leveledPosition) == 2) {
                                map.put(targetPosition, new Pair<>(Quantum.blockControlRod, 0));

                                // Place piston base to push control rods in.
                                int rotationMetadata = 0;
                                Vector3 offset = new Vector3(x, 0, z).normalize();

                                for (ForgeDirection checkDir : ForgeDirection.VALID_DIRECTIONS) {
                                    if (offset.x == checkDir.offsetX && offset.y == checkDir.offsetY && offset.z == checkDir.offsetZ) {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                    }
                                }

                                map.put(targetPosition.clone().translate(offset), new Pair<Block, Integer>(Blocks.sticky_piston, rotationMetadata));
                            } else if (x == -radius || x == radius || z == -radius || z == radius) {
                                map.put(targetPosition, new Pair<>(Blocks.glass, 0));
                            } else if (x == 0 && z == 0) {
                                map.put(targetPosition, new Pair<>(Quantum.blockReactorCell, 0));
                            } else {
                                map.put(targetPosition, new Pair<>(Blocks.water, 0));
                            }
                        } else if (targetPosition.distance(leveledPosition) < 2) {
                            map.put(targetPosition, new Pair<>(Quantum.blockElectricTurbine, 0));
                        }
                    }
                }
            }
        }

        return map;
    }
}