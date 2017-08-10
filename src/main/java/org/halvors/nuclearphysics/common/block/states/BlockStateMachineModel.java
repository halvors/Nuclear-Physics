package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel.EnumMachineModel;

public class BlockStateMachineModel extends BlockStateFacing {
    public static final PropertyEnum<EnumMachineModel> TYPE = PropertyEnum.create("type", EnumMachineModel.class);

    public BlockStateMachineModel(BlockMachineModel block) {
        super(block, TYPE);
    }
}