package org.halvors.quantum.common.block.reactor.fusion;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.utility.FluidUtility;

import javax.annotation.Nonnull;

public class BlockPlasmaHeater extends BlockContainerQuantum {
    public BlockPlasmaHeater() {
        super("plasma_heater", Material.IRON);

        //setTextureName(Reference.PREFIX + "machine");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        return FluidUtility.playerActivatedFluidItem(world, pos, player, side);
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TilePlasmaHeater();
    }
}

