package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.DirectionalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.BlockStateContainer;
import org.halvors.nuclearphysics.common.block.BlockRotatable;

public class BlockStateFacing extends BlockStateContainer {
    public static final IProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BlockStateFacing(final BlockRotatable block, final EnumProperty typeProperty) {

    }
}
