package org.halvors.quantum.atomic.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import org.halvors.quantum.atomic.common.Quantum;

public class ItemBlockQuantum extends ItemBlock {
    public ItemBlockQuantum(Block block) {
        super(block);

        setRegistryName(block.getRegistryName());
        setCreativeTab(Quantum.getCreativeTab());
    }
}
