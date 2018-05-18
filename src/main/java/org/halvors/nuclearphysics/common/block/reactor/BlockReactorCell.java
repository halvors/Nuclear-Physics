package org.halvors.nuclearphysics.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockReactorCell extends BlockInventory {
    public BlockReactorCell() {
        super("reactor_cell", Material.iron);

        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(Reference.PREFIX + "machine");
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final ItemStack itemStack = tileReactorCell.getStackInSlot(0);

            // Spawn particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
            if (itemStack != null && tileReactorCell.getTemperature() >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
                world.spawnParticle("cloud", x + world.rand.nextInt(2), y + 1, z + world.rand.nextInt(2), 0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;
            final ItemStack itemStack = player.getHeldItem();
            final FluidStack fluidStack = tileReactorCell.getTank().getFluid();
            final ItemStack itemStackInSlot = tileReactorCell.getStackInSlot(0);

            if (player.isSneaking()) {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack == null && itemStackInSlot != null) {
                    player.inventory.addItemStackToInventory(itemStackInSlot.copy());
                    tileReactorCell.setInventorySlotContents(0, null);

                    return true;
                }
            } else {
                if (!ModFluids.fluidStackPlasma.isFluidEqual(fluidStack) && itemStack != null && itemStackInSlot == null && itemStack.getItem() instanceof IReactorComponent) {
                    tileReactorCell.setInventorySlotContents(0, itemStack.copy());
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
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileReactorCell();
    }
}
