package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.block.BlockContainerBase;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;

import javax.annotation.Nonnull;

public class BlockElectricTurbine extends BlockContainerBase {
    public BlockElectricTurbine() {
        super("electric_turbine", Material.IRON);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileElectricTurbine) {
            final TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (InventoryUtility.hasUsableWrench(player, pos)) {
                return tileTurbine.getMultiBlock().toggleConstruct();
            }
        }

        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileElectricTurbine) {
            TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;
            tileTurbine.getMultiBlock().deconstruct();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileElectricTurbine();
    }
}
