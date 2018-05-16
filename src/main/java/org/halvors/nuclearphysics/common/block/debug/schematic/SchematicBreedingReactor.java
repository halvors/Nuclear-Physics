package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import java.util.HashMap;

public class SchematicBreedingReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.breeding_reactor.name";
    }

    @Override
    public HashMap<BlockPos, IBlockState> getStructure(EnumFacing facing, int size) {
        final HashMap<BlockPos, IBlockState> map = new HashMap<>();

        int radius = Math.max(size, 2);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                map.put(new BlockPos(x, 0, z), Blocks.WATER.getDefaultState());
            }
        }

        radius--;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                final BlockPos pos = new BlockPos(x, 0, z);

                if (VectorUtility.getMagnitude(pos) <= 2) {
                    if (!((x == -radius || x == radius) && (z == -radius || z == radius))) {
                        map.put(new BlockPos(x, 0, z), ModBlocks.blockReactorCell.getDefaultState());
                        map.put(new BlockPos(x, -1, z), ModBlocks.blockThermometer.getDefaultState());
                        map.put(new BlockPos(x, -3, z), ModBlocks.blockSiren.getDefaultState());
                        map.put(new BlockPos(x, -2, z), Blocks.REDSTONE_WIRE.getDefaultState());
                    } else {
                        map.put(new BlockPos(x, -1, z), ModBlocks.blockControlRod.getDefaultState());
                        map.put(new BlockPos(x, -2, z), Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, EnumFacing.UP));
                    }
                }
            }
        }

        map.put(BlockPos.ORIGIN.down(2), Blocks.STONE.getDefaultState());
        map.put(BlockPos.ORIGIN.down(3), Blocks.STONE.getDefaultState());
        map.put(BlockPos.ORIGIN, ModBlocks.blockReactorCell.getDefaultState());

        return map;
    }
}