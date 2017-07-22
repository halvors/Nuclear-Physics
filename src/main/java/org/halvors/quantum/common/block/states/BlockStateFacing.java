package org.halvors.quantum.common.block.states;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;

public class BlockStateFacing extends BlockStateContainer {
    public static final PropertyDirection facingProperty = PropertyDirection.create("facing");

    public BlockStateFacing(Block block, PropertyEnum typeProperty) {
        super(block, facingProperty, typeProperty);
    }

    public BlockStateFacing(Block block) {
        super(block, facingProperty);
    }
}
