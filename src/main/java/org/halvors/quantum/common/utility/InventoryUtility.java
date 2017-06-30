package org.halvors.quantum.common.utility;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

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

        dropItemStack(position.world, position, toInsert, 20);

        return null;
    }

    public static ItemStack putStackInInventory(IInventory inventory, ItemStack itemStack, int side, boolean force) {
        ItemStack toInsert = itemStack != null ? itemStack.copy() : null;

        if (toInsert != null) {
            if (!(inventory instanceof ISidedInventory)) {
                return putStackInInventory(inventory, toInsert, force);
            }

            ISidedInventory sidedInventory = (ISidedInventory)inventory;
            int[] slots = sidedInventory.getSlotsForFace(EnumFacing.getOrientation(side).getOpposite().ordinal());

            if ((slots != null) && (slots.length != 0)) {
                for (int get = 0; get <= slots.length - 1; get++) {
                    int slotID = slots[get];

                    if ((force) || (sidedInventory.isItemValidForSlot(slotID, toInsert)) || (sidedInventory.canInsertItem(slotID, toInsert, EnumFacing.getOrientation(side).getOpposite().ordinal()))) {
                        ItemStack inSlot = inventory.getStackInSlot(slotID);

                        if (inSlot == null) {
                            inventory.setInventorySlotContents(slotID, toInsert);
                            return null;
                        }

                        if (inSlot.isItemEqual(toInsert) && inSlot.stackSize < inSlot.getMaxStackSize()) {
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
            int metadata = world.getBlockMetadata(x, y, z);
            Block block = world.getBlock(x, y, z);

            if (block != Blocks.air) {
                ArrayList<ItemStack> items = block.getDrops(world, x, y, z, metadata, 0);

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
        dropItemStack(world, position, itemStack, delay);
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
