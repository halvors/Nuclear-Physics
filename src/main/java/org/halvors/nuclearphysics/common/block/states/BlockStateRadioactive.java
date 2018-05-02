package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.IStringSerializable;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockRadioactive;

public class BlockStateRadioactive extends BlockStateContainer {
    public static final PropertyEnum<EnumRadioactive> TYPE = PropertyEnum.create("type", EnumRadioactive.class);

    public BlockStateRadioactive(BlockRadioactive block) {
        super(block, TYPE);
    }

    public enum EnumRadioactive implements IStringSerializable {
        DIRT(Material.GROUND, SoundType.GROUND),
        GRASS(Material.GRASS, SoundType.GROUND),
        URANIUM_ORE(Material.ROCK);

        private final Material material;
        private final SoundType soundType;

        EnumRadioactive(final Material material, final SoundType soundType) {
            this.material = material;
            this.soundType = soundType;
        }

        EnumRadioactive(final Material material) {
            this(material, SoundType.STONE);
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }

        public Material getMaterial() {
            return material;
        }

        public SoundType getSoundType() {
            return soundType;
        }
    }
}