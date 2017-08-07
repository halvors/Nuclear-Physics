package org.halvors.quantum.atomic.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine.EnumMachine;

public class BlockStateMachine extends BlockStateFacing {
    public static final PropertyEnum<BlockMachine.EnumMachine> typeProperty = PropertyEnum.create("type", EnumMachine.class);

    public BlockStateMachine(BlockMachine block) {
        super(block, typeProperty);
    }
}