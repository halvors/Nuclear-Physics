package org.halvors.quantum.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.particle.TileAccelerator;

public class BlockAccelerator extends BlockRotatable {
    public BlockAccelerator() {
        super("accelerator", Material.iron);

        setTextureName(Reference.PREFIX + "accelerator");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(Quantum.getInstance(), 0, world, x, y, z);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAccelerator();
    }
}
