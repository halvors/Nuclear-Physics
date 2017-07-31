package org.halvors.quantum.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.HashMap;

public class SchematicFissionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fissionReactor.name";
    }

    @Override
    public HashMap<Vector3, IBlockState> getStructure(EnumFacing direction, int size) {
        HashMap<Vector3, IBlockState> map = new HashMap<>();
        int radius = 2;

        if (size <= 1) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Vector3 targetPosition = new Vector3(x, 0, z);
                    map.put(targetPosition, Blocks.WATER.getDefaultState());
                }
            }

            radius--;

            // Create turbines and control rods.
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    Vector3 targetPosition = new Vector3(x, 1, z);
                    map.put(targetPosition, QuantumBlocks.blockElectricTurbine.getDefaultState());

                    if (!(x == -radius || x == radius && z == -radius || z == radius) && new Vector3(x, 0, z).getMagnitude() <= 1) {
                        map.put(new Vector3(x, -1, z), QuantumBlocks.blockControlRod.getDefaultState());
                        //map.put(new Vector3(x, -2, z), new Pair<>(Blocks.STICKY_PISTON, 1));
                        map.put(new Vector3(x, -2, z), Blocks.STICKY_PISTON.getDefaultState());
                    }
                }
            }

            map.put(new Vector3(0, -3, 0), QuantumBlocks.blockSiren.getDefaultState());
            map.put(new Vector3(0, -2, 0), Blocks.REDSTONE_WIRE.getDefaultState());
            map.put(new Vector3(), QuantumBlocks.blockReactorCell.getDefaultState());
        } else {
            for (int y = 0; y < size; y++) {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        Vector3 targetPosition = new Vector3(x, y, z);
                        Vector3 leveledPosition = new Vector3(0, y, 0);

                        if (y < size - 1) {
                            if (targetPosition.distance(leveledPosition) == 2) {
                                map.put(targetPosition, QuantumBlocks.blockControlRod.getDefaultState());

                                // Place piston base to push control rods in.
                                int rotationMetadata = 0;
                                Vector3 offset = new Vector3(x, 0, z).normalize();

                                for (EnumFacing checkDir : EnumFacing.VALUES) {
                                    if (offset.x == checkDir.getFrontOffsetX() && offset.y == checkDir.getFrontOffsetY() && offset.z == checkDir.getFrontOffsetZ()) {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                    }
                                }

                                map.put(targetPosition.clone().translate(offset), Blocks.STICKY_PISTON.getStateFromMeta(rotationMetadata));
                            } else if (x == -radius || x == radius || z == -radius || z == radius) {
                                map.put(targetPosition, Blocks.GLASS.getDefaultState());
                            } else if (x == 0 && z == 0) {
                                map.put(targetPosition, QuantumBlocks.blockReactorCell.getDefaultState());
                            } else {
                                map.put(targetPosition, Blocks.WATER.getDefaultState());
                            }
                        } else if (targetPosition.distance(leveledPosition) < 2) {
                            map.put(targetPosition, QuantumBlocks.blockElectricTurbine.getDefaultState());
                        }
                    }
                }
            }
        }

        return map;
    }
}