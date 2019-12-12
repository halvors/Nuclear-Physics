package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockReactorCell extends BlockInventory {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return false;
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
    @OnlyIn(Dist.CLIENT)
    public void randomTick(BlockState state, World world, BlockPos pos, Random random) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final ItemStack itemStack = tileReactorCell.getInventory().getStackInSlot(0);

            // Spawn particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
            if (!itemStack.isEmpty() && tileReactorCell.getTemperature() >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
                world.spawnParticle(ParticleTypes.CLOUD, pos.getX() + world.rand.nextInt(2), pos.getY() + 1, pos.getZ() + world.rand.nextInt(2), 0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final FluidStack fluidStack = tileReactorCell.getTank().getFluid();
            final IItemHandlerModifiable inventory = tileReactorCell.getInventory();
            final ItemStack itemStack = player.getHeldItemMainhand();
            final ItemStack itemStackInSlot = inventory.getStackInSlot(0);

            if (player.isSneaking()) {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack.isEmpty() && !itemStackInSlot.isEmpty()) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStackInSlot.copy());
                    inventory.setStackInSlot(0, ItemStack.EMPTY);

                    return true;
                }
            } else {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && !itemStack.isEmpty() && itemStackInSlot.isEmpty() && itemStack.getItem() instanceof IReactorComponent) {
                    inventory.insertItem(0, itemStack.copy(), false);
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                } else {
                    PlayerUtility.openGui(player, world, pos);
                }

                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileReactorCell(name);
    }
}
