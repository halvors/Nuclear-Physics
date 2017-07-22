package org.halvors.quantum.common.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.BlockRotatable;

public abstract class BlockMachine extends BlockRotatable {
    public BlockMachine(String name) {
        super(name, Material.IRON);

        //setTextureName(Reference.PREFIX + "machine");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            player.openGui(Quantum.getInstance(), 0, world, pos.getX(), pos.getY(), pos.getZ());

            return true;
        }

        return false;
    }
}
