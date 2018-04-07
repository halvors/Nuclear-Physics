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
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.client.render.block.BlockRenderingHandler;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.init.ModFluids;
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
    public void registerIcons(IIconRegister iconRegister) {
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
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        final TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;
            ItemStack itemStack = tileReactorCell.getStackInSlot(0);

            // Spawn particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
            if (itemStack != null && tileReactorCell.getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                world.spawnParticle("cloud", x + world.rand.nextInt(2), y + 1, z + world.rand.nextInt(2), 0, 0.1, 0);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;
            ItemStack itemStack = player.getHeldItem();
            FluidStack fluidStack = tileReactorCell.getTank().getFluid();
            ItemStack itemStackInSlot = tileReactorCell.getStackInSlot(0);

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
                    PlayerUtility.openGui(player, world, x, y, z);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileReactorCell();
    }
}
