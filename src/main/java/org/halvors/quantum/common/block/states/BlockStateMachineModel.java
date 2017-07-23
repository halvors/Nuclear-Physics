package org.halvors.quantum.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.block.machine.BlockMachine.EnumModelMachine;

public class BlockStateMachineModel extends BlockStateFacing {
    public static final PropertyEnum<EnumModelMachine> typeProperty = PropertyEnum.create("type", EnumModelMachine.class);

    public BlockStateMachineModel(BlockMachine block) {
        super(block, typeProperty);
    }
}