package org.halvors.quantum.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.halvors.quantum.common.init.QuantumBlocks;
import org.halvors.quantum.common.utility.position.Position;

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

                    if (!(x == -radius || x == radius && z == -radius || z == radius) && new Position(x, 0, z).getMagnitude() <= 1) {
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
                        Position targetPosition = new Position(x, y, z);
                        Position leveledPosition = new Position(0, y, 0);
                        BlockPos targetPos = new BlockPos(targetPosition.getX(), targetPosition.getY(), targetPosition.getZ());

                        if (y < size - 1) {
                            if (targetPosition.distance(leveledPosition) == 2) {
                                map.put(targetPos, QuantumBlocks.blockControlRod.getDefaultState());

                                // Place piston base to push control rods in.
                                int rotationMetadata = 0;
                                Position offset = new Position(x, 0, z).normalize();

                                for (EnumFacing checkDir : EnumFacing.VALUES) {
                                    if (offset.getX() == checkDir.getFrontOffsetX() && offset.getY() == checkDir.getFrontOffsetY() && offset.getZ() == checkDir.getFrontOffsetZ()) {
                                        rotationMetadata = checkDir.getOpposite().ordinal();
                                    }
                                }

                                map.put(new BlockPos(targetPosition.translate(offset).getX(), targetPosition.translate(offset).getY(), targetPosition.translate(offset).getZ()), Blocks.STICKY_PISTON.getStateFromMeta(rotationMetadata));
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