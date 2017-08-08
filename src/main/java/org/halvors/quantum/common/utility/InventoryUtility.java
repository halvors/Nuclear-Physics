package org.halvors.quantum.common.utility;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryUtility {
    public static void incrStackSize(IItemHandlerModifiable itemHandler, int slot) {
        ItemStack itemStack = itemHandler.getStackInSlot(slot);

        if (itemStack != null) {
            itemHandler.insertItem(slot, ItemHandlerHelper.copyStackWithSize(itemStack, itemStack.stackSize++), false);
        }
    }

    public static void decrStackSize(IItemHandlerModifiable itemHandler, int slot) {
        ItemStack itemStack = itemHandler.getStackInSlot(slot);

        if (itemStack != null) {
            itemHandler.extractItem(slot, 1, false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Old code, maybe not used anymore. //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void dropItemStack(World world, BlockPos pos, ItemStack itemStack) {
        dropItemStack(world, pos, itemStack, 10);
    }

    public static void dropItemStack(World world, BlockPos pos, ItemStack itemStack, int delay) {
        dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack, delay);
    }

    public static void dropItemStack(World world, double x, double y, double z, ItemStack itemStack, int delay) {
        if (!world.isRemote && itemStack != null) {
            float motion = 0.7F;
            double motionX = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motionY = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;
            double motionZ = (world.rand.nextFloat() * motion) + (1.0F - motion) * 0.5D;

            EntityItem entityItem = new EntityItem(world, x + motionX, y + motionY, z + motionZ, itemStack);

            if (itemStack.hasTagCompound()) {
                entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
            }

            entityItem.setPickupDelay(delay);
            world.spawnEntity(entityItem);
        }
    }
}
