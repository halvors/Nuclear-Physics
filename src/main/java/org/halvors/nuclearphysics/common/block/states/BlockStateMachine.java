package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;

public class BlockStateMachine extends BlockStateFacing {
    public static final PropertyEnum<EnumMachine> TYPE = PropertyEnum.create("type", EnumMachine.class);

    public BlockStateMachine(BlockMachine block) {
        super(block, TYPE);
    }
}