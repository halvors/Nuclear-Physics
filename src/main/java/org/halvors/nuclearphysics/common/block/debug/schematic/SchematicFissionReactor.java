package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;

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
                    final Position targetPosition = new Position(x, y, z);
                    final BlockPos blockTargetPos = new BlockPos(targetPosition.getIntX(), targetPosition.getIntY(), targetPosition.getIntZ());
                    final Position leveledPosition = new Position(0, y, 0);

                    if (y < size - 1) {
                        if (targetPosition.distance(leveledPosition) == 2) {
                            map.put(new BlockPos(targetPosition.getIntX(), targetPosition.getIntY(), targetPosition.getIntZ()), new Pair<>(ModBlocks.blockControlRod, 0));

                            // Place piston base to push control rods in.
                            final Position offset = new Position(x, 0, z).normalize();

                            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                                if (offset.getX() == side.offsetX && offset.getY() == side.offsetY && offset.getZ() == side.offsetZ) {
                                    facing = side.getOpposite();
                                }
                            }

                            final Position pos = targetPosition.translate(offset);
                            final BlockPos blockPos = new BlockPos(pos.getIntX(), pos.getIntY(), pos.getIntZ());

                            map.put(blockPos, new Pair<>(Blocks.sticky_piston, facing.ordinal()));
                            map.put(blockPos.offset(facing.getOpposite()), new Pair<>(Blocks.lever, 0));
                        } else if (x == -radius || x == radius || z == -radius || z == radius) {
                            map.put(blockTargetPos, new Pair<>(Blocks.glass, 0));
                        } else if (x == 0 && z == 0) {
                            map.put(blockTargetPos, new Pair<>(ModBlocks.blockReactorCell, 0));
                        } else {
                            map.put(blockTargetPos, new Pair<>(Blocks.water, 0));
                        }
                    } else if (targetPosition.distance(leveledPosition) < 2) {
                        map.put(blockTargetPos, new Pair<>(ModBlocks.blockElectricTurbine, 0));
                    }
                }
            }
        }

        return map;
    }
}