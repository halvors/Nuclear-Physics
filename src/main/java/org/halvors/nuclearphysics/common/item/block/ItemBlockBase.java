package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.nuclearphysics.NuclearPhysics;

public class ItemBlockBase extends ItemBlock {
    public ItemBlockBase(final Block block) {
        super(block);

        setRegistryName(block.getRegistryName());
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }
}
