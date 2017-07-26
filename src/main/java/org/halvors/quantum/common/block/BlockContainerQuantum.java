package org.halvors.quantum.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import org.halvors.quantum.common.Quantum;

public abstract class BlockContainerQuantum extends BlockContainer {
    protected String name;

    public BlockContainerQuantum(String name, Material material) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Quantum.getCreativeTab());
    }

    public void registerItemModel(ItemBlock itemBlock) {
        Quantum.getProxy().registerItemRenderer(itemBlock, 0, name);
    }
}
