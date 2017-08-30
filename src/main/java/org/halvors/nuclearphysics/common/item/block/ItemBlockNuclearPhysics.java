package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public class ItemBlockNuclearPhysics extends ItemBlock {
    public ItemBlockNuclearPhysics(Block block) {
        super(block);

        setRegistryName(block.getRegistryName());
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }
}
