package org.halvors.quantum.atomic.common.block.debug.schematic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;
import org.halvors.quantum.atomic.common.block.states.BlockStateElectromagnet;
import org.halvors.quantum.atomic.common.init.QuantumBlocks;

import java.util.HashMap;

public class SchematicAccelerator implements ISchematic {
    @Override
    public String getName() {
        return "schematic.accelerator.name";
    }

    @Override
    public HashMap<BlockPos, IBlockState> getStructure(EnumFacing direction, int size) {
        HashMap<BlockPos, IBlockState> map = new HashMap<>();
        int radius = size;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new BlockPos(x, y, z), QuantumBlocks.blockElectromagnet.getDefaultState());
                    }
                }
            }
        }

        radius = size - 2;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new BlockPos(x, y, z), QuantumBlocks.blockElectromagnet.getDefaultState());
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
                            map.put(new BlockPos(x, y, z), QuantumBlocks.blockElectromagnet.getDefaultState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.GLASS));
                        } else {
                            map.put(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        return map;
    }
}