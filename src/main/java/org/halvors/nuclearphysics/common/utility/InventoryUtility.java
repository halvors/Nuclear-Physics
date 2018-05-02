package org.halvors.nuclearphysics.common.utility;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InventoryUtility {
    public static NBTTagCompound getNBTTagCompound(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return itemStack.getTagCompound();
        }

        return null;
    }

    public static ItemStack getItemStackWithNBT(final Block block, final World world, final int x, final int y, final int z) {
        if (block != null) {
            final ItemStack dropStack = new ItemStack(block, block.quantityDropped(world.rand), block.damageDropped(world.getBlockMetadata(x, y, z)));
            final TileEntity tile = world.getTileEntity(x, y, z);

            if (tile != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                tile.writeToNBT(tag);
                dropStack.setTagCompound(tag);
            }

            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(final Block block, final World world, final int x, final int y, final int z) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            final ItemStack itemStack = getItemStackWithNBT(block, world, x, y, z);

            if (itemStack != null) {
                InventoryUtility.dropItemStack(world, x, y, z, itemStack);
            }
        }
    }

    public static void dropItemStack(final World world, final int x, final int y, final int z, final ItemStack itemStack) {
        dropItemStack(world, x, y, z, itemStack, 10);
    }

    public static void dropItemStack(final World world, final double x, final double y, final double z, final ItemStack itemStack, final int delay) {
        if (!world.isRemote && itemStack != null) {
            final float motion = 0.7F;
            final double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            final EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            entityItem.delayBeforeCanPickup = delay;
            world.spawnEntityInWorld(entityItem);
        }
    }
}
