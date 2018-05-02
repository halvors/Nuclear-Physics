package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nonnull;

public class BlockElectricTurbine extends BlockContainerBase {
    public BlockElectricTurbine() {
        super("electric_turbine", Material.IRON);

        setHardness(0.6F);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileElectricTurbine) {
            final TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;

            if (WrenchUtility.hasUsableWrench(player, hand, pos)) {
                return tileTurbine.getMultiBlock().toggleConstruct();
            }
        }

        return false;
    }

    @Override
    public void breakBlock(final @Nonnull World world, final @Nonnull BlockPos pos, final @Nonnull IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileElectricTurbine) {
            final TileElectricTurbine tileTurbine = (TileElectricTurbine) tile;
            tileTurbine.getMultiBlock().deconstruct();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(final @Nonnull World world, final @Nonnull IBlockState state) {
        return new TileElectricTurbine();
    }
}
