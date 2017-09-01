package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public class BlockBase extends Block {
    protected String name;

    public BlockBase(String name, Material material) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }

    public void registerBlockModel() {

    }

    public void registerItemModel(ItemBlock itemBlock) {
        NuclearPhysics.getProxy().registerItemRenderer(itemBlock, 0, name);
    }
}
