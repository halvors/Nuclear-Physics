package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public class ItemBlockBase extends ItemBlock {
    public ItemBlockBase(final Block block) {
        super(block);

        setCreativeTab(NuclearPhysics.getCreativeTab());
    }
}
