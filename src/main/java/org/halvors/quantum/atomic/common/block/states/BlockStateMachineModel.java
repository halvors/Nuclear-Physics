package org.halvors.quantum.atomic.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel.EnumMachineModel;

public class BlockStateMachineModel extends BlockStateFacing {
    public static final PropertyEnum<EnumMachineModel> typeProperty = PropertyEnum.create("type", EnumMachineModel.class);

    public BlockStateMachineModel(BlockMachineModel block) {
        super(block, typeProperty);
    }
}