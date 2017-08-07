package org.halvors.quantum.atomic.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.halvors.quantum.atomic.common.QuantumBlocks;
import org.halvors.quantum.atomic.common.utility.transform.vector.Vector3;

import java.util.HashMap;

public class SchematicFissionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fissionReactor.name";
    }

    @Override
    public HashMap<BlockPos, IBlockState> getStructure(EnumFacing direction, int size) {
        HashMap<BlockPos, IBlockState> map = new HashMap<>();
        int radius = 2;

        if (size <= 1) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    map.put(new BlockPos(x, 0, z), Blocks.WATER.getDefaultState());
                }
            }

            radius--;

            // Create turbines and control rods.
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    map.put(new BlockPos(x, 1, z), QuantumBlocks.blockElectricTurbine.getDefaultState());

                    if (!(x == -radius || x == radius && z == -radius || z == radius) && new Vector3(x, 0, z).getMagnitude() <= 1) {
                        map.put(new BlockPos(x, -1, z), QuantumBlocks.blockControlRod.getDefaultState());
                        //map.put(new Vector3(x, -2, z), new Pair<>(Blocks.STICKY_PISTON, 1));
                        map.put(new BlockPos(x, -2, z), Blocks.STICKY_PISTON.getDefaultState());
                    }
                }
            }

            map.put(new BlockPos(0, -3, 0), QuantumBlocks.blockSiren.getDefaultState());
            map.put(new BlockPos(0, -2, 0), Blocks.REDSTONE_WIRE.getDefaultState());
            map.put(new BlockPos(0, 0, 0), QuantumBlocks.blockReactorCell.getDefaultState());
        } else {
            for (int y = 0; y < size; y++) {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        Vector3 targetPosition = new Vector3(x, y, z);
                        Vector3 leveledPosition = new Vector3(0, y, 0);
                        BlockPos targetPos = new BlockPos(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());

                        if (y < size - 1) {
                            if (targetPosition.distance(leveledPosition) == 2) {
                                map.put(targetPos, QuantumBlocks.blockControlRod.getDefaultState());

                                // Place piston base to push control rods in.
                                int rotationMetadata = 0;
                                Vector3 offset = new Vector3(x, 0, z).normalize();

                                for (EnumFacing checkDir : EnumFacing.VALUES) {
                                    if (offset.x == checkDir.getFrontOffsetX() && offset.y == checkDir.getFrontOffsetY() && offset.z == checkDir.getFrontOffsetZ()) {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                    }
                                }

                                map.put(new BlockPos(targetPosition.clone().translate(offset).getX(), targetPosition.clone().translate(offset).getY(), targetPosition.clone().translate(offset).getZ()), Blocks.STICKY_PISTON.getStateFromMeta(rotationMetadata));
                            } else if (x == -radius || x == radius || z == -radius || z == radius) {
                                map.put(targetPos, Blocks.GLASS.getDefaultState());
                            } else if (x == 0 && z == 0) {
                                map.put(targetPos, QuantumBlocks.blockReactorCell.getDefaultState());
                            } else {
                                map.put(targetPos, Blocks.WATER.getDefaultState());
                            }
                        } else if (targetPosition.distance(leveledPosition) < 2) {
                            map.put(targetPos, QuantumBlocks.blockElectricTurbine.getDefaultState());
                        }
                    }
                }
            }
        }

        return map;
    }
}