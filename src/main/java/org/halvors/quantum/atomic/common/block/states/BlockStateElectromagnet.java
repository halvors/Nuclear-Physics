package org.halvors.quantum.atomic.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;

public class BlockStateElectromagnet extends BlockStateContainer {
    public static final PropertyEnum<EnumElectromagnet> typeProperty = PropertyEnum.create("type", EnumElectromagnet.class);

    public BlockStateElectromagnet(BlockElectromagnet block) {
        super(block, typeProperty);
    }
}