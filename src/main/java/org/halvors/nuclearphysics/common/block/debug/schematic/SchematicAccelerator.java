package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.block.states.BlockStateElectromagnet;
import org.halvors.nuclearphysics.common.block.states.BlockStateElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import java.util.HashMap;

public class SchematicAccelerator implements ISchematic {
    @Override
    public String getName() {
        return "schematic.accelerator.name";
    }

    @Override
    public HashMap<BlockPos, BlockState> getStructure(final Direction direction, int size) {
        final HashMap<BlockPos, BlockState> map = new HashMap<>();

        if (size < 4) {
            size = 4;
        }

        int radius = Math.max(size, 3);

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new BlockPos(x, y, z), ModBlocks.blockElectromagnet.getDefaultState());
                    }
                }
            }
        }

        radius = size - 2;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new BlockPos(x, y, z), ModBlocks.blockElectromagnet.getDefaultState());
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
                            map.put(new BlockPos(x, y, z), ModBlocks.blockElectromagnet.getDefaultState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.GLASS));
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