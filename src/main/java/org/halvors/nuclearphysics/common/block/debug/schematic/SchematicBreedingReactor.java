package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import java.util.HashMap;

public class SchematicBreedingReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.breeding_reactor.name";
    }

    @Override
    public HashMap<BlockPos, Pair<Block, Integer>> getStructure(final ForgeDirection facing, final int size) {
        final HashMap<BlockPos, Pair<Block, Integer>> map = new HashMap<>();

        int radius = Math.max(size, 2);

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                map.put(new BlockPos(x, 0, z), new Pair<>(Blocks.water, 0));
            }
        }

        radius--;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                final BlockPos pos = new BlockPos(x, 0, z);

                if (VectorUtility.getMagnitude(pos) <= 2) {
                    if (!((x == -radius || x == radius) && (z == -radius || z == radius))) {
                        map.put(new BlockPos(x, 0, z), new Pair<>(ModBlocks.blockReactorCell, 0));
                        map.put(new BlockPos(x, -1, z), new Pair<>(ModBlocks.blockThermometer, 0));
                        map.put(new BlockPos(x, -3, z), new Pair<>(ModBlocks.blockSiren, 0));
                        map.put(new BlockPos(x, -2, z), new Pair<>(Blocks.redstone_wire, 0));
                    } else {
                        map.put(new BlockPos(x, -1, z), new Pair<>(ModBlocks.blockControlRod, 0));
                        map.put(new BlockPos(x, -2, z), new Pair<>(Blocks.sticky_piston, ForgeDirection.UP.ordinal()));
                    }
                }
            }
        }

        map.put(BlockPos.ORIGIN.down(2), new Pair<>(Blocks.stone, 0));
        map.put(BlockPos.ORIGIN.down(3), new Pair<>(Blocks.stone, 0));
        map.put(BlockPos.ORIGIN, new Pair<>(ModBlocks.blockReactorCell, 0));

        return map;
    }
}