package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;

public class BlockStateElectromagnet extends BlockStateConnectedTexture {
    public static final PropertyEnum<EnumElectromagnet> TYPE = PropertyEnum.create("type", EnumElectromagnet.class);

    public BlockStateElectromagnet(BlockElectromagnet block) {
        super(block, TYPE);
    }
}