package org.halvors.quantum.atomic.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.halvors.quantum.atomic.common.QuantumBlocks;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet;

import java.util.HashMap;

public class SchematicFusionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fusionReactor.name";
    }

    @Override
    public HashMap<BlockPos, IBlockState> getStructure(EnumFacing direction, int size) {
        HashMap<BlockPos, IBlockState> map = new HashMap<>();

        // Fusion Torus.
        int radius = size + 2;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= size; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    double magnitude = Math.sqrt(x * x + z * z);

                    if (!map.containsKey(pos)) {
                        map.put(pos, Blocks.AIR.getDefaultState());
                    }

                    if (magnitude <= radius) {
                        if (y == 0 || y == size) {
                            if (magnitude >= 1) {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * Math.PI) * size / 2;
                                map.put(pos.add(0, yDeviation, 0), QuantumBlocks.blockElectromagnet.getStateFromMeta(BlockElectromagnet.EnumElectromagnet.GLASS.ordinal()));
                            }
                        } else if (magnitude > radius - 1) {
                            map.put(pos, QuantumBlocks.blockElectromagnet.getDefaultState());
                        }
                    }
                }
            }
        }

        // Fusion Core
        for (int y = 0; y < size; y++) {
            map.put(new BlockPos(0, y, 0), QuantumBlocks.blockReactorCell.getDefaultState());
            map.put(new BlockPos(1, y, 0), QuantumBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(0, y, 1), QuantumBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(0, y, -1), QuantumBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(-1, y, 0), QuantumBlocks.blockElectromagnet.getDefaultState());
        }

        map.put(new BlockPos(0, 0, 0), QuantumBlocks.blockReactorCell.getDefaultState());

        return map;
    }
}
