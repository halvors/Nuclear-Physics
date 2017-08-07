package org.halvors.quantum.atomic.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import org.halvors.quantum.atomic.common.Quantum;

public class BlockQuantum extends Block {
    protected String name;

    public BlockQuantum(String name, Material material) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Quantum.getCreativeTab());
    }

    public void registerBlockModel() {

    }

    public void registerItemModel(ItemBlock itemBlock) {
        Quantum.getProxy().registerItemRenderer(itemBlock, 0, name);
    }
}
