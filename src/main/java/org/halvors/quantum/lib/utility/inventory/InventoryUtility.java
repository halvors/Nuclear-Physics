package org.halvors.quantum.lib.utility.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.utility.MachinePlayer;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtility {
    public static IInventory checkChestInv(IInventory inv) {
        if (inv instanceof TileEntityChest) {
            TileEntityChest main = (TileEntityChest) inv;
            TileEntityChest adj = null;

            if (main.adjacentChestXNeg != null) {
                adj = main.adjacentChestXNeg;
            } else if (main.adjacentChestXPos != null) {
                adj = main.adjacentChestXPos;
            } else if (main.adjacentChestZNeg != null) {
                adj = main.adjacentChestZNeg;
            } else if (main.adjacentChestZPos != null) {
                adj = main.adjacentChestZPos;
            }

            if (adj != null) {
                return new InventoryLargeChest("", main, adj);
            }
        }

        return inv;
    }

    public static ItemStack putStackInInventory(IInventory inventory, ItemStack toInsert, boolean force) {
        inventory = checkChestInv(inventory);

        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            if ((force) || (inventory.isItemValidForSlot(slot, toInsert))) {
                ItemStack slot_stack = inventory.getStackInSlot(slot);

                if (slot_stack == null) {
                    inventory.setInventorySlotContents(slot, toInsert);
                    return null;
                }

                if ((slot_stack.isItemEqual(toInsert)) && (slot_stack.stackSize < slot_stack.getMaxStackSize())) {
                    if (slot_stack.stackSize + toInsert.stackSize <= slot_stack.getMaxStackSize()) {
                        ItemStack toSet = toInsert.copy();
                        toSet.stackSize += slot_stack.stackSize;

                        inventory.setInventorySlotContents(slot, toSet);
                        return null;
                    }

                    int rejects = slot_stack.stackSize + toInsert.stackSize - slot_stack.getMaxStackSize();

                    ItemStack toSet = toInsert.copy();
                    toSet.stackSize = slot_stack.getMaxStackSize();

                    ItemStack remains = toInsert.copy();
                    remains.stackSize = rejects;

                    inventory.setInventorySlotContents(slot, toSet);

                    toInsert = remains;
                }
            }
        }

        return toInsert;
    }

    public static ItemStack putStackInInventory(VectorWorld position, ItemStack toInsert, int side, boolean force) {
        TileEntity tile = position.getTileEntity();

        if (tile instanceof IInventory) {
            return putStackInInventory((IInventory)tile, toInsert, force);
        }

        dropItemStack(position.world, position, toInsert, 20, 0.0F);

        return null;
    }

    public static ItemStack putStackInInventory(IInventory inventory, ItemStack itemStack, int side, boolean force) {
        ItemStack toInsert = itemStack != null ? itemStack.copy() : null;

        if (toInsert != null) {
            if (!(inventory instanceof ISidedInventory)) {
                return putStackInInventory(inventory, toInsert, force);
            }

            ISidedInventory sidedInventory = (ISidedInventory)inventory;
            int[] slots = sidedInventory.getSlotsForFace(ForgeDirection.getOrientation(side).getOpposite().ordinal());

            if ((slots != null) && (slots.length != 0)) {
                for (int get = 0; get <= slots.length - 1; get++) {
                    int slotID = slots[get];

                    if ((force) || (sidedInventory.isItemValidForSlot(slotID, toInsert)) || (sidedInventory.canInsertItem(slotID, toInsert, ForgeDirection.getOrientation(side).getOpposite().ordinal()))) {
                        ItemStack inSlot = inventory.getStackInSlot(slotID);

                        if (inSlot == null) {
                            inventory.setInventorySlotContents(slotID, toInsert);
                            return null;
                        }

                        if ((inSlot.isItemEqual(toInsert)) && (inSlot.stackSize < inSlot.getMaxStackSize())) {
                            if (inSlot.stackSize + toInsert.stackSize <= inSlot.getMaxStackSize()) {
                                ItemStack toSet = toInsert.copy();
                                toSet.stackSize += inSlot.stackSize;

                                inventory.setInventorySlotContents(slotID, toSet);
                                return null;
                            }

                            int rejects = inSlot.stackSize + toInsert.stackSize - inSlot.getMaxStackSize();

                            ItemStack toSet = toInsert.copy();
                            toSet.stackSize = inSlot.getMaxStackSize();

                            ItemStack remains = toInsert.copy();
                            remains.stackSize = rejects;

                            inventory.setInventorySlotContents(slotID, toSet);

                            toInsert = remains;
                        }
                    }
                }
            }
        }

        return toInsert;
    }

    public static ItemStack takeTopItemFromInventory(IInventory inventory, int side) {
        if (!(inventory instanceof ISidedInventory)) {
            for (int i = inventory.getSizeInventory() - 1; i >= 0; i--) {
                if (inventory.getStackInSlot(i) != null) {
                    ItemStack toSend = inventory.getStackInSlot(i).copy();
                    toSend.stackSize = 1;

                    inventory.decrStackSize(i, 1);

                    return toSend;
                }
            }
        } else {
            ISidedInventory sidedInventory = (ISidedInventory)inventory;
            int[] slots = sidedInventory.getSlotsForFace(side);

            if (slots != null) {
                for (int get = slots.length - 1; get >= 0; get--) {
                    int slotID = slots[get];

                    if (sidedInventory.getStackInSlot(slotID) != null) {
                        ItemStack toSend = sidedInventory.getStackInSlot(slotID);
                        toSend.stackSize = 1;

                        if (sidedInventory.canExtractItem(slotID, toSend, side)) {
                            sidedInventory.decrStackSize(slotID, 1);

                            return toSend;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static ItemStack takeTopBlockFromInventory(IInventory inventory, int side) {
        if (!(inventory instanceof ISidedInventory)) {
            for (int i = inventory.getSizeInventory() - 1; i >= 0; i--) {
                if ((inventory.getStackInSlot(i) != null) && ((inventory.getStackInSlot(i).getItem() instanceof ItemBlock))) {
                    ItemStack toSend = inventory.getStackInSlot(i).copy();
                    toSend.stackSize = 1;

                    inventory.decrStackSize(i, 1);

                    return toSend;
                }
            }
        } else {
            ISidedInventory sidedInventory = (ISidedInventory)inventory;
            int[] slots = sidedInventory.getSlotsForFace(side);

            if (slots != null) {
                for (int get = slots.length - 1; get >= 0; get--) {
                    int slotID = slots[get];

                    if ((sidedInventory.getStackInSlot(slotID) != null) && ((inventory.getStackInSlot(slotID).getItem() instanceof ItemBlock))) {
                        ItemStack toSend = sidedInventory.getStackInSlot(slotID);
                        toSend.stackSize = 1;

                        if (sidedInventory.canExtractItem(slotID, toSend, side)) {
                            sidedInventory.decrStackSize(slotID, 1);

                            return toSend;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void dropBlockAsItem(World world, Vector3 position) {
        dropBlockAsItem(world, position.intX(), position.intY(), position.intZ(), false);
    }

    public static void dropBlockAsItem(World world, int x, int y, int z, boolean destroy) {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);
            Block block = world.getBlock(x, y, z);

            if (block != Blocks.air) {
                ArrayList<ItemStack> items = block.getDrops(world, x, y, z, meta, 0);

                for (ItemStack itemStack : items) {
                    dropItemStack(world, new Vector3(x, y, z), itemStack, 10);
                }
            }

            if (destroy) {
                world.setBlockToAir(x, y, z);
            }
        }
    }

    public static void dropItemStack(VectorWorld position, ItemStack itemStack) {
        dropItemStack(position.world, position, itemStack);
    }

    public static void dropItemStack(World world, Vector3 position, ItemStack itemStack) {
        dropItemStack(world, position, itemStack, 10);
    }

    public static void dropItemStack(World world, Vector3 position, ItemStack itemStack, int delay) {
        dropItemStack(world, position, itemStack, delay, 0.0F);
    }

    public static void dropItemStack(World world, Vector3 position, ItemStack itemStack, int delay, float randomAmount) {
        dropItemStack(world, position.x, position.y, position.z, itemStack, delay, randomAmount);
    }

    public static void dropItemStack(World world, double x, double y, double z, ItemStack itemStack, int delay, float randomAmount) {
        assert (world.isRemote) : ("Inventory Utility [Can not drop ItemStacks client side @" + x + "x " + y + "y " + z + "z]");
        assert (itemStack == null) : ("Inventory Utility [Can not drop null ItemStacks @" + x + "x " + y + "y " + z + "z]");

        if (!world.isRemote && itemStack != null) {
            double randomX = 0.0D;
            double randomY = 0.0D;
            double randomZ = 0.0D;

            if (randomAmount > 0.0F) {
                randomX = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
                randomY = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
                randomZ = world.rand.nextFloat() * randomAmount + (1.0F - randomAmount) * 0.5D;
            }

            EntityItem entityitem = new EntityItem(world, x + randomX, y + randomY, z + randomZ, itemStack);

            if (randomAmount <= 0.0F) {
                entityitem.motionX = 0.0D;
                entityitem.motionY = 0.0D;
                entityitem.motionZ = 0.0D;
            }

            if (itemStack.hasTagCompound()) {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            }

            entityitem.delayBeforeCanPickup = delay;
            world.spawnEntityInWorld(entityitem);
        }
    }

    public static boolean placeItemBlock(World world, int x, int y, int z, ItemStack itemStack, int side)
    {
        if (itemStack != null) {
            try {
                Vector3 rightClickPos = new Vector3(x, y, z);

                if (world.isAirBlock(x, y, z)) {
                    rightClickPos.translate(ForgeDirection.getOrientation(side));
                }

                side ^= 0x1;

                return MachinePlayer.useItemAt(itemStack, world, x, y - 1, z, side);
            } catch (Exception e) {
                e.printStackTrace();

                if (world.getBlock(x, y, z).getItem(world, x, y, z) == itemStack.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static ItemStack decrStackSize(ItemStack stack, int amount) {
        if (stack != null) {
            ItemStack itemStack = stack.copy();

            if (itemStack.stackSize <= amount) {
                return null;
            }

            itemStack.stackSize -= amount;

            if (itemStack.stackSize <= 0) {
                return null;
            }

            return itemStack;
        }

        return null;
    }

    public static void consumeHeldItem(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();

        if ((player != null) && (stack != null)) {
            stack = stack.copy();

            if (stack.getItem().hasContainerItem()) {
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

    public static ItemStack consumeStack(ItemStack stack) {
        if (stack. 	stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack)) {
                return stack.getItem().getContainerItem(stack);
            }
        } else {
            return stack.splitStack(1);
        }

        return null;
    }

    public static boolean stacksMatchExact(ItemStack stackA, ItemStack stackB) {
        if (stackA == null && stackB == null) {
            return true;
        }

        if (stackA != null && stackB != null) {
            return (stackA.isItemEqual(stackB)) && (stackA.stackSize == stackB.stackSize);
        }

        return false;
    }

    public static int getStackCount(ItemStack stack, IInventory inv, int[] slots) {
        int count = 0;

        if (stack != null) {
            List<Integer> slot_list = new ArrayList<>();

            if (((slots != null ? 1 : 0) & (slots.length > 0 ? 1 : 0)) != 0) {
                for (int i = 0; i < slots.length; i++) {
                    slot_list.add(slots[i]);
                }
            }

            for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
                if (slot_list.isEmpty() || slot_list.contains(Integer.valueOf(slot))) {
                    if ((inv.getStackInSlot(slot) != null) && (inv.getStackInSlot(slot).isItemEqual(stack))) {
                        count += inv.getStackInSlot(slot).stackSize;
                    }
                }
            }
        }

        return count;
    }

    public static int getStackCount(Class<?> compare, IInventory inv) {
        return getStackCount(compare, inv);
    }

    public static int getStackCount(Class<?> compare, IInventory inv, int[] slots) {
        int count = 0;

        if (compare != null) {
            List<Integer> slot_list = new ArrayList<>();

            if (((slots != null ? 1 : 0) & (slots.length > 0 ? 1 : 0)) != 0) {
                for (int i = 0; i < slots.length; i++) {
                    slot_list.add(slots[i]);
                }
            }

            for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
                if (slot_list.isEmpty() || slot_list.contains(Integer.valueOf(slot))) {
                    if (inv.getStackInSlot(slot) != null && compare.isInstance(inv.getStackInSlot(slot).getItem())) {
                        count += inv.getStackInSlot(slot).stackSize;
                    }
                }
            }
        }
        return count;
    }
}
