package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.block.BlockRotatable;
import org.halvors.nuclearphysics.common.item.block.reactor.ItemBlockThermometer;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockThermometer extends BlockRotatable {
    public BlockThermometer() {
        super("thermometer", Material.PISTON);

        setHardness(0.6F);
    }

    @Override
    public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;
            final ItemStack itemStack = player.getHeldItemMainhand();

            if (!itemStack.isEmpty()) {
                if (WrenchUtility.hasUsableWrench(player, hand, pos)) {
                    if (player.isSneaking()) {
                        tileThermometer.setThreshold(tileThermometer.getThershold() - 10);
                    } else {
                        tileThermometer.setThreshold(tileThermometer.getThershold() + 10);
                    }

                    return true;
                }
            } else {
                if (player.isSneaking()) {
                    tileThermometer.setThreshold(tileThermometer.getThershold() + 100);
                } else {
                    tileThermometer.setThreshold(tileThermometer.getThershold() - 100);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase entity, final ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(pos);

        // Fetch saved coordinates from ItemBlockThermometer and apply them to the block.
        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;
            final ItemBlockThermometer itemBlockThermometer = (ItemBlockThermometer) itemStack.getItem();
            tileThermometer.setTrackCoordinate(itemBlockThermometer.getSavedCoordinate(itemStack));
        }

        super.onBlockPlacedBy(world, pos, state, entity, itemStack);
    }

    @Override
    public boolean removedByPlayer(final @Nonnull IBlockState state, final World world, final @Nonnull BlockPos pos, final @Nonnull EntityPlayer player, final boolean willHarvest) {
        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            final ItemStack itemStack = InventoryUtility.getItemStackWithNBT(state, world, pos);
            InventoryUtility.dropItemStack(world, pos, itemStack);
        }

        return world.setBlockToAir(pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(final IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, final int fortune) {
        return new ArrayList<>();
    }

    @Override
    public TileEntity createTileEntity(final @Nonnull World world, final @Nonnull IBlockState state) {
        return new TileThermometer();
    }
}
