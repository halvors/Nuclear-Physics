package org.halvors.nuclearphysics.common.utility;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;

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

    public static ItemStack getItemStackWithNBT(final Block block, final World world, final BlockPos pos) {
        if (block != null) {
            final ItemStack dropStack = new ItemStack(block, block.quantityDropped(world.rand), block.damageDropped(pos.getBlockMetadata(world)));
            final TileEntity tile = pos.getTileEntity(world);

            if (tile != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                tile.writeToNBT(tag);
                dropStack.setTagCompound(tag);
            }

            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(final Block block, final World world, final BlockPos pos) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            final ItemStack itemStack = getItemStackWithNBT(block, world, pos);

            if (itemStack != null) {
                dropItemStack(world, pos, itemStack);
            }
        }
    }

    public static void dropItemStack(final World world, final BlockPos pos, final ItemStack itemStack) {
        dropItemStack(world, pos, itemStack, 10);
    }

    public static void dropItemStack(final World world, final BlockPos pos, final ItemStack itemStack, final int delay) {
        if (!world.isRemote && itemStack != null) {
            final float motion = 0.7F;
            final double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            final EntityItem entityItem = new EntityItem(world, pos.getX() + motionX, pos.getY() + motionY, pos.getZ() + motionZ, itemStack);

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            entityItem.delayBeforeCanPickup = delay;
            world.spawnEntityInWorld(entityItem);
        }
    }
}
