package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet;

public class BlockStateElectromagnet extends BlockStateConnectedTexture {
    public static final PropertyEnum<EnumElectromagnet> TYPE = PropertyEnum.create("type", EnumElectromagnet.class);

    public BlockStateElectromagnet(final BlockElectromagnet block) {
        super(block, TYPE);
    }

    public enum EnumElectromagnet implements IStringSerializable {
        NORMAL,
        GLASS;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}