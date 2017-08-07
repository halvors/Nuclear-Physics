package org.halvors.quantum.common.block.states;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;

public class BlockStateFacing extends BlockStateContainer {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockStateFacing(Block block) {
        super(block, FACING);
    }

    public BlockStateFacing(Block block, PropertyEnum typeProperty) {
        super(block, FACING, typeProperty);
    }
}
