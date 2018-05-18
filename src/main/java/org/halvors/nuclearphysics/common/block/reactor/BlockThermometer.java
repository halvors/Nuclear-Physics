package org.halvors.nuclearphysics.common.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockRotatable;
import org.halvors.nuclearphysics.common.item.block.reactor.ItemBlockThermometer;
import org.halvors.nuclearphysics.common.tile.reactor.TileThermometer;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.WrenchUtility;

import java.util.ArrayList;

public class BlockThermometer extends BlockRotatable {
    @SideOnly(Side.CLIENT)
    private static IIcon iconSide;

    public BlockThermometer() {
        super("thermometer", Material.piston);

        setHardness(0.6F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconSide = iconRegister.registerIcon(Reference.PREFIX + "machine");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int metadata) {
        switch (side) {
            case 1:
                return blockIcon;

            default:
                return iconSide;
        }
    }

    @Override
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float hitX, final float hitY, final float hitZ) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        final ItemStack itemStack = player.getHeldItem();

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            if (itemStack != null) {
                if (WrenchUtility.hasUsableWrench(player, pos)) {
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
    public void onBlockPlacedBy(final World world, final int x, final int y, final int z, final EntityLivingBase entity, final ItemStack itemStack) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        // Fetch saved coordinates from ItemBlockThermometer and apply them to the block.
        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;
            final ItemBlockThermometer itemBlockThermometer = (ItemBlockThermometer) itemStack.getItem();
            tileThermometer.setTrackCoordinate(itemBlockThermometer.getSavedCoordinate(itemStack));
        }

        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
    }

    @Override
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        final BlockPos pos = new BlockPos(x, y, z);
        final Block block = pos.getBlock(world);

        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            final ItemStack itemStack = InventoryUtility.getItemStackWithNBT(block, world, pos);
            InventoryUtility.dropItemStack(world, pos, itemStack);
        }

        return pos.setBlockToAir(world);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(final IBlockAccess world, final int x, final int y, final int z, final int side) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return super.isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        return new ArrayList<>();
    }

    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileThermometer();
    }
}
