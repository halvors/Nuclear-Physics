package org.halvors.quantum.common.utility;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

public class InventoryUtility {
    public static void incrStackSize(IItemHandler itemHandler, int slot) {
        ItemStack itemStack = itemHandler.getStackInSlot(slot).copy();
        itemStack.stackSize++;

        itemHandler.insertItem(slot, itemStack, false);
    }

    public static void decrStackSize(IItemHandler itemHandler, int slot) {
        ItemStack itemStack = itemHandler.getStackInSlot(slot).copy();
        itemStack.stackSize--;

        itemHandler.insertItem(slot, itemStack, false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Old code, maybe not used anymore. //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void dropItemStack(VectorWorld position, ItemStack itemStack) {
        dropItemStack(position.world, position, itemStack);
    }

    public static void dropItemStack(World world, Vector3 position, ItemStack itemStack) {
        dropItemStack(world, position, itemStack, 10);
    }

    public static void dropItemStack(World world, Vector3 position, ItemStack itemStack, int delay) {
        dropItemStack(world, position.getX(), position.getY(), position.getZ(), itemStack, delay);
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

    public static void consumeHeldItem(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();

        if (stack != null) {
            stack = stack.copy();

            if (stack.getItem().hasContainerItem(stack)) {
                if (stack.stackSize == 1) {
                    stack = stack.getItem().getContainerItem(stack);
                } else {
                    player.inventory.addItemStackToInventory(stack.getItem().getContainerItem(stack.splitStack(1)));
                }
            } else if (stack.stackSize == 1) {
                stack = null;
            } else {
                stack.splitStack(1);
            }

            player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
        }
    }
}
