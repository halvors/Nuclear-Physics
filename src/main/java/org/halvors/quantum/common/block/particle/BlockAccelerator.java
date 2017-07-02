package org.halvors.quantum.common.block.particle;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.particle.TileAccelerator;

public class BlockAccelerator extends BlockRotatable {
    public BlockAccelerator() {
        super("accelerator", Material.IRON);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(Quantum.getInstance(), 0, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAccelerator();
    }
}
