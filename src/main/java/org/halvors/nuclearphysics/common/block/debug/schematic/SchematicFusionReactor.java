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

public class SchematicFusionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fusion_reactor.name";
    }

    @Override
    public HashMap<BlockPos, BlockState> getStructure(Direction direction, int size) {
        final HashMap<BlockPos, BlockState> map = new HashMap<>();

        // Fusion Torus.
        final int radius = size + 2;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= size; y++) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    final double magnitude = Math.sqrt(x * x + z * z);

                    if (!map.containsKey(pos)) {
                        map.put(pos, Blocks.AIR.getDefaultState());
                    }

                    if (magnitude <= radius) {
                        if (y == 0 || y == size) {
                            if (magnitude >= 1) {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * Math.PI) * size / 2;

                                map.put(pos.add(0, yDeviation, 0), ModBlocks.blockElectromagnet.getDefaultState().withProperty(BlockStateElectromagnet.TYPE, EnumElectromagnet.GLASS));
                            }
                        } else if (magnitude > radius - 1) {
                            map.put(pos, ModBlocks.blockElectromagnet.getDefaultState());
                        }
                    }
                }
            }
        }

        // Fusion Core
        for (int y = 0; y < size; y++) {
            map.put(new BlockPos(0, y, 0), ModBlocks.blockReactorCell.getDefaultState());
            map.put(new BlockPos(1, y, 0), ModBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(0, y, 1), ModBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(0, y, -1), ModBlocks.blockElectromagnet.getDefaultState());
            map.put(new BlockPos(-1, y, 0), ModBlocks.blockElectromagnet.getDefaultState());
        }

        map.put(BlockPos.ZERO, ModBlocks.blockReactorCell.getDefaultState());

        return map;
    }
}
