package org.halvors.quantum.common.utility;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

public class InventoryUtility {
    public static void incrStackSize(IItemHandlerModifiable itemHandler, int slot) {
        ItemStack itemStack = itemHandler.getStackInSlot(slot);

        itemHandler.insertItem(slot, ItemHandlerHelper.copyStackWithSize(itemStack, itemStack.getCount() + 1), false);
    }

    public static void decrStackSize(IItemHandlerModifiable itemHandler, int slot) {
        itemHandler.extractItem(slot, 1, false);
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
                entityItem.getItem().setTagCompound(itemStack.getTagCompound().copy());
            }

            entityItem.setPickupDelay(delay);
            world.spawnEntity(entityItem);
        }
    }

    public static void consumeHeldItem(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();

        if (!stack.isEmpty()) {
            stack = stack.copy();

            if (stack.getItem().hasContainerItem(stack)) {
                if (stack.getCount() == 1) {
                    stack = stack.getItem().getContainerItem(stack);
                } else {
                    player.inventory.addItemStackToInventory(stack.getItem().getContainerItem(stack.splitStack(1)));
                }
            } else if (stack.getCount() == 1) {
                stack = null;
            } else {
                stack.splitStack(1);
            }

            player.inventory.setInventorySlotContents(player.inventory.currentItem, stack);
        }
    }
}
