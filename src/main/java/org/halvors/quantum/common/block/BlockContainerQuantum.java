package org.halvors.quantum.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;
import org.halvors.quantum.Quantum;

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

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
