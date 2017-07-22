package org.halvors.quantum.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.quantum.common.block.machine.BlockMachineModel;
import org.halvors.quantum.common.block.machine.BlockMachineModel.EnumModelMachine;

public class BlockStateMachineModel extends BlockStateFacing {
    public static final PropertyEnum<EnumModelMachine> typeProperty = PropertyEnum.create("type", EnumModelMachine.class);

    public BlockStateMachineModel(BlockMachineModel block) {
        super(block, typeProperty);
    }
}