package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import org.halvors.nuclearphysics.common.block.BlockRotatable;
import org.halvors.nuclearphysics.common.item.block.reactor.ItemBlockThermometer;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockThermometer extends BlockRotatable {
    public BlockThermometer() {
        super("thermometer", Material.PISTON);

        setHardness(0.6F);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
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
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack itemStack) {
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
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            final ItemStack itemStack = InventoryUtility.getItemStackWithNBT(state, world, pos);
            InventoryUtility.dropItemStack(world, pos, itemStack);
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(BlockState state, IBlockReader block, BlockPos pos, Direction side) {
        final TileEntity tile = block.getTileEntity(pos);

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return super.getWeakPower(state, block, pos, side);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileThermometer();
    }
}
