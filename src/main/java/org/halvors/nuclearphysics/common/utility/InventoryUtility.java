package org.halvors.nuclearphysics.common.utility;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class InventoryUtility {
    public static NBTTagCompound getNBTTagCompound(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return itemStack.getTagCompound();
        }

        return null;
    }

    public static ItemStack getItemStackWithNBT(Block block, World world, int x, int y, int z) {
        if (block != null) {
            ItemStack dropStack = new ItemStack(block, block.quantityDropped(world.rand), block.damageDropped(world.getBlockMetadata(x, y, z)));
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tile.writeToNBT(tag);
                dropStack.setTagCompound(tag);
            }

            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(Block block, World world, int x, int y, int z) {
        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            ItemStack itemStack = getItemStackWithNBT(block, world, x, y, z);

            if (itemStack != null) {
                InventoryUtility.dropItemStack(world, x, y, z, itemStack);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Old code, maybe not used anymore. //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void dropItemStack(World world, int x, int y, int z, ItemStack itemStack) {
        dropItemStack(world, x, y, z, itemStack, 10);
    }

    public static void dropItemStack(World world, double x, double y, double z, ItemStack itemStack, int delay) {
        if (!world.isRemote && itemStack != null) {
            float motion = 0.7F;
            double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            entityItem.delayBeforeCanPickup = delay;
            world.spawnEntityInWorld(entityItem);
        }
    }
}
