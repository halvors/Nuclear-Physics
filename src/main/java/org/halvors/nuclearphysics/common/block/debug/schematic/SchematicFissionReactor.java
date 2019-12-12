package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import java.util.HashMap;

public class SchematicFissionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fission_reactor.name";
    }

    @Override
    public HashMap<BlockPos, BlockState> getStructure(Direction direction, int size) {
        final HashMap<BlockPos, BlockState> map = new HashMap<>();
        final int radius = 2;

        // We do not support high reactor towers yet. Forcing HEIGHT.
        size = 2;

        for (int y = 0; y < size; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    final BlockPos targetPos = new BlockPos(x, y, z);
                    final BlockPos leveledPos = new BlockPos(0, y, 0);

                    if (y < size - 1) {
                        if (targetPos.getDistance(leveledPos.getX(), leveledPos.getY(), leveledPos.getZ()) == 2) {
                            map.put(targetPos, ModBlocks.blockControlRod.getDefaultState());

                            // Place piston base to push control rods in.
                            final BlockPos offsetPos = VectorUtility.normalize(new BlockPos(x, 0, z));

                            for (Direction side : Direction.values()) {
                                if (offsetPos.getX() == side.getXOffset() && offsetPos.getY() == side.getYOffset() && offsetPos.getZ() == side.getZOffset()) {
                                    direction = side.getOpposite();
                                }
                            }

                            final BlockPos pos = targetPos.add(offsetPos);
                            map.put(pos, Blocks.STICKY_PISTON.getDefaultState().withProperty(PistonBlock.FACING, direction));
                            map.put(pos.offset(direction.getOpposite()), Blocks.LEVER.getDefaultState().withProperty(LeverBlock.FACING, (facing.getAxis() == Direction.Axis.X ? EnumOrientation.UP_X : EnumOrientation.UP_Z)));
                        } else if (x == -radius || x == radius || z == -radius || z == radius) {
                            map.put(targetPos, Blocks.GLASS.getDefaultState());
                        } else if (x == 0 && z == 0) {
                            map.put(targetPos, ModBlocks.blockReactorCell.getDefaultState());
                        } else {
                            map.put(targetPos, Blocks.WATER.getDefaultState());
                        }
                    } else if (targetPos.getDistance(leveledPos.getX(), leveledPos.getY(), leveledPos.getZ()) < 2) {
                        map.put(targetPos, ModBlocks.blockElectricTurbine.getDefaultState());

                    }
                }
            }
        }

        return map;
    }
}