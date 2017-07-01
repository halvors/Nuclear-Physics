package org.halvors.quantum.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;

public class BlockQuantum extends BlockContainer {
    public BlockQuantum(String name, Material material) {
        super(material);

        setUnlocalizedName(name);
        setRegistryName(Reference.ID, name);
        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return null;
    }
}
