package org.halvors.nuclearphysics.common.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryUtility {
    public static void incrStackSize(final IItemHandlerModifiable itemHandler, final int slot) {
        final ItemStack itemStack = itemHandler.getStackInSlot(slot);

        if (itemStack != null) {
            itemHandler.insertItem(slot, ItemHandlerHelper.copyStackWithSize(itemStack, itemStack.stackSize++), false);
        }
    }

    public static void decrStackSize(final IItemHandlerModifiable itemHandler, final int slot) {
        final ItemStack itemStack = itemHandler.getStackInSlot(slot);

        if (itemStack != null) {
            itemHandler.extractItem(slot, 1, false);
        }
    }

    public static NBTTagCompound getNBTTagCompound(final ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return itemStack.getTagCompound();
        }

        return null;
    }

    public static ItemStack getItemStackWithNBT(final IBlockState state, final World world, final BlockPos pos) {
        if (state != null) {
            final Block block = state.getBlock();
            final ItemStack dropStack = new ItemStack(block, block.quantityDropped(state, 0, world.rand), block.damageDropped(state));
            final TileEntity tile = world.getTileEntity(pos);

            if (tile != null) {
                final NBTTagCompound tag = new NBTTagCompound();
                tile.writeToNBT(tag);
                dropStack.setTagCompound(tag);
            }

            return dropStack;
        }

        return null;
    }

    public static void dropBlockWithNBT(final IBlockState state, final World world, final BlockPos pos) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            final ItemStack itemStack = getItemStackWithNBT(state, world, pos);

            if (itemStack != null) {
                InventoryUtility.dropItemStack(world, pos, itemStack);
            }
        }
    }

    public static void readFromNBT(NBTTagCompound tag, IItemHandlerModifiable inventory) {
        if (tag.getTagId("Inventory") == Constants.NBT.TAG_LIST) {
            final NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.tagCount(); i++) {
                final NBTTagCompound slotTag = (NBTTagCompound) tagList.get(i);
                final byte slot = slotTag.getByte("Slot");

                if (slot < inventory.getSlots()) {
                    inventory.setStackInSlot(slot, ItemStack.loadItemStackFromNBT(slotTag));
                }
            }
        }
    }

    public static void dropItemStack(final World world, final BlockPos pos, final ItemStack itemStack) {
        dropItemStack(world, pos, itemStack, 10);
    }

    public static void dropItemStack(final World world, final BlockPos pos, final ItemStack itemStack, final int delay) {
        dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack, delay);
    }

    public static void dropItemStack(final World world, final double x, final double y, final double z, final ItemStack itemStack, final int delay) {
        if (!world.isRemote && itemStack != null) {
            final float motion = 0.7F;
            final double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            final double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            final EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
            }

            entityItem.setPickupDelay(delay);
            world.spawnEntity(entityItem);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static boolean equalsWildcard(ItemStack itemStack, ItemStack itemStackCheck) {
        if (itemStack == null || itemStackCheck == null) {
            return itemStackCheck == itemStack;
        }

        return itemStack.getItem() == itemStackCheck.getItem() && (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemStack.getItemDamage() == itemStackCheck.getItemDamage());
    }

    public static boolean equalsWildcardWithNBT(ItemStack itemStack, ItemStack itemStackCheck) {
        final boolean wildcard = equalsWildcard(itemStack, itemStackCheck);

        if (itemStack == null || itemStackCheck == null) {
            return wildcard;
        }

        final NBTTagCompound itemStackTagCompound = itemStack.getTagCompound();

        if (itemStackTagCompound != null) {
            return wildcard && (!itemStack.hasTagCompound() ? !itemStackCheck.hasTagCompound() : (itemStackTagCompound == itemStackCheck.getTagCompound() || itemStackTagCompound.equals(itemStackCheck.getTagCompound())));
        }

        return false;
    }
}
