package org.halvors.nuclearphysics.common.block.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockReactorCell extends BlockInventory {
    public BlockReactorCell() {
        super("reactor_cell", Material.IRON);

        setHardness(1.0F);
        setResistance(1.0F);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(final IBlockState state, @Nonnull final BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
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
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random random) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final ItemStack itemStack = tileReactorCell.getInventory().getStackInSlot(0);

            // Spawn particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
            if (itemStack != null && tileReactorCell.getTemperature() >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
                world.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + world.rand.nextInt(2), pos.getY() + 1, pos.getZ() + world.rand.nextInt(2), 0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final ItemStack itemStack, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final FluidStack fluidStack = tileReactorCell.getTank().getFluid();
            final IItemHandlerModifiable inventory = tileReactorCell.getInventory();
            final ItemStack itemStackInSlot = inventory.getStackInSlot(0);

            if (player.isSneaking()) {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack == null && itemStackInSlot != null) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStackInSlot.copy());
                    inventory.setStackInSlot(0, null);

                    return true;
                }
            } else {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack != null && itemStackInSlot == null && itemStack.getItem() instanceof IReactorComponent) {
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

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull final World world, @Nonnull final IBlockState state) {
        return new TileReactorCell(name);
    }
}
