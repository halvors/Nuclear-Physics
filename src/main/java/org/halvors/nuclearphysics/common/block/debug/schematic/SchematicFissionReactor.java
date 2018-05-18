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

public class SchematicFissionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fission_reactor.name";
    }

    @Override
    public HashMap<BlockPos, Pair<Block, Integer>> getStructure(ForgeDirection facing, int size) {
        final HashMap<BlockPos, Pair<Block, Integer>>  map = new HashMap<>();
        final int radius = 2;

        // We do not support high reactor towers yet. Forcing height.
        size = 2;

        for (int y = 0; y < size; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    final BlockPos targetPos = new BlockPos(x, y, z);
                    final BlockPos leveledPos = new BlockPos(0, y, 0);

                    if (y < size - 1) {
                        if (targetPos.getDistance(leveledPos.getX(), leveledPos.getY(), leveledPos.getZ()) == 2) {
                            map.put(targetPos, new Pair<>(ModBlocks.blockControlRod, 0));

                            // Place piston base to push control rods in.
                            final BlockPos offsetPos = VectorUtility.normalize(new BlockPos(x, 0, z));

                            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                                if (offsetPos.getX() == side.offsetX && offsetPos.getY() == side.offsetY && offsetPos.getZ() == side.offsetZ) {
                                    facing = side.getOpposite();
                                }
                            }

                            final BlockPos pos = targetPos.add(offsetPos);
                            map.put(pos, new Pair<>(Blocks.sticky_piston, facing.ordinal()));
                            map.put(pos.offset(facing.getOpposite()), new Pair<>(Blocks.lever, 0));
                        } else if (x == -radius || x == radius || z == -radius || z == radius) {
                            map.put(targetPos, new Pair<>(Blocks.glass, 0));
                        } else if (x == 0 && z == 0) {
                            map.put(targetPos, new Pair<>(ModBlocks.blockReactorCell, 0));
                        } else {
                            map.put(targetPos, new Pair<>(Blocks.water, 0));
                        }
                    } else if (targetPos.getDistance(leveledPos.getX(), leveledPos.getY(), leveledPos.getZ()) < 2) {
                        map.put(targetPos, new Pair<>(ModBlocks.blockElectricTurbine, 0));
                    }
                }
            }
        }

        return map;
    }
}