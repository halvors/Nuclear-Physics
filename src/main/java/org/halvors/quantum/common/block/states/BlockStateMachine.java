package org.halvors.quantum.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.block.machine.BlockMachine.EnumMachine;

public class BlockStateMachine extends BlockStateFacing {
    public static final PropertyEnum<BlockMachine.EnumMachine> TYPE = PropertyEnum.create("type", EnumMachine.class);

    public BlockStateMachine(BlockMachine block) {
        super(block, TYPE);
    }
}