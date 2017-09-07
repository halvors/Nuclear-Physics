package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import org.halvors.nuclearphysics.common.block.BlockRotatable;

public class BlockStateFacing extends BlockStateContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockStateFacing(BlockRotatable block) {
        super(block, FACING);
    }

    public BlockStateFacing(BlockRotatable block, PropertyEnum typeProperty) {
        super(block, FACING, typeProperty);
    }
}
