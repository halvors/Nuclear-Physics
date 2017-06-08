package org.halvors.quantum.common.accelerator;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.lib.prefab.block.BlockRotatable;

public class BlockAccelerator extends BlockRotatable {
    public BlockAccelerator() {
        super(Material.iron);

    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int metadata = world.getBlockMetadata(x, y, z);

        if (!world.isRemote) {
            player.openGui(Quantum.getInstance(), 0, world, x, y, z);
        }

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAccelerator();
    }
}
