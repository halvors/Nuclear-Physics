package org.halvors.quantum.lib.prefab.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.utility.WrenchUtility;
import org.halvors.quantum.lib.utility.inventory.InventoryUtility;

import java.lang.reflect.Method;

public abstract class BlockAdvanced extends Block {
    public BlockAdvanced(Material material) {
        super(material);

        setHardness(0.6F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if (WrenchUtility.isUsableWrench(entityPlayer, entityPlayer.inventory.getCurrentItem(), x, y, z)) {
            WrenchUtility.damageWrench(entityPlayer, entityPlayer.inventory.getCurrentItem(), x, y, z);
            if (entityPlayer.isSneaking()) {
                if (onSneakUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ)) {
                    return true;
                }
            }

            if (onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ)) {
                return true;
            }
            return false;
        }

        if (entityPlayer.isSneaking()) {
            if (onSneakMachineActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ)) {
                return true;
            }
        }

        return onMachineActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }


    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public boolean onSneakMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        return onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

    public boolean interactCurrentItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        if (current != null) {
            if ((stackInInventory == null) || (stackInInventory.isItemEqual(current))) {
                return insertCurrentItem(inventory, slotID, player);
            }
        }

        return extractItem(inventory, slotID, player);
    }

    public boolean insertCurrentItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        if (current != null) {
            if (stackInInventory == null || stackInInventory.isItemEqual(current)) {
                if (inventory.isItemValidForSlot(slotID, current)) {
                    if (isControlDown(player)) {
                        if (stackInInventory == null) {
                            inventory.setInventorySlotContents(slotID, current.splitStack(1));
                        } else {
                            stackInInventory.stackSize++;
                            current.stackSize--;
                        }
                    } else {
                        if (stackInInventory == null) {
                            inventory.setInventorySlotContents(slotID, current);
                        } else {
                            stackInInventory.stackSize += current. 	stackSize;
                            current.stackSize = 0;
                        }

                        current = null;
                    }

                    if (current == null || current.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    public boolean extractItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);

        if (stackInInventory != null) {
            if (isControlDown(player)) {
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory.splitStack(1), 0);
            } else {
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory, 0);
                stackInInventory = null;
            }

            if (stackInInventory == null || stackInInventory.stackSize <= 0) {
                inventory.setInventorySlotContents(slotID, null);
            }

            return true;
        }

        return false;
    }

    public boolean isControlDown(EntityPlayer player) {
        try {
            Class ckm = Class.forName("codechicken.multipart.ControlKeyModifer");
            Method m = ckm.getMethod("isControlDown", new Class[] { EntityPlayer.class });
            return ((Boolean)m.invoke(null, new Object[] { player })).booleanValue();
        } catch (Exception e) {

        }

        return false;
    }
}
