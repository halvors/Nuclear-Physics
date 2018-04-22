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
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconSide = iconRegister.registerIcon(Reference.PREFIX + "machine");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        switch (side) {
            case 1:
                return blockIcon;

            default:
                return iconSide;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        final TileEntity tile = world.getTileEntity(x, y, z);
        final ItemStack itemStack = player.getHeldItem();

        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;

            if (itemStack != null) {
                if (WrenchUtility.hasUsableWrench(player, x, y, z)) {
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        final TileEntity tile = world.getTileEntity(x, y, z);

        // Fetch saved coordinates from ItemBlockThermometer and apply them to the block.
        if (tile instanceof TileThermometer) {
            final TileThermometer tileThermometer = (TileThermometer) tile;
            final ItemBlockThermometer itemBlockThermometer = (ItemBlockThermometer) itemStack.getItem();
            tileThermometer.setTrackCoordinate(itemBlockThermometer.getSavedCoordinate(itemStack));
        }

        super.onBlockPlacedBy(world, x, y, z, entity, itemStack);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        Block block = world.getBlock(x, y, z);

        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            ItemStack itemStack = InventoryUtility.getItemStackWithNBT(block, world, x, y, z);
            InventoryUtility.dropItemStack(world, x, y, z, itemStack);
        }

        return world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileThermometer();
    }
}
