package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.world.chunk.BlockStateContainer;
import org.halvors.nuclearphysics.common.block.BlockRotatable;

public class BlockStateFacing extends BlockStateContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockStateFacing(final BlockRotatable block) {
        super(block, FACING);
    }

    public BlockStateFacing(final BlockRotatable block, final PropertyEnum typeProperty) {
        super(block, FACING, typeProperty);
    }
}
