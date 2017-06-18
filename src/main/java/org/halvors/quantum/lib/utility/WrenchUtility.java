package org.halvors.quantum.lib.utility;

import buildcraft.api.tools.IToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;
//import resonant.core.content.ItemScrewdriver;

public class WrenchUtility {
    public static boolean isHoldingWrench(EntityPlayer player) {
        return isWrench(player.getHeldItem());
    }

    public static boolean isWrench(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem() instanceof IToolWrench) {
                return true;
            }

            /*
            if (itemStack.getItem() instanceof ItemScrewdriver) {
                return true;
            }
            */

            try {
                Class wrenchClass = itemStack.getItem().getClass();

                if ((wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench")) || (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))) {
                    return true;
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    public static boolean isUsableWrench(EntityPlayer player, int x, int y, int z) {
        return isUsableWrench(player, player.getHeldItem(), x, y, z);
    }

    public static boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z) {
        if (player != null && itemStack != null) {
            if (itemStack.getItem() instanceof IToolWrench) {
                return ((IToolWrench) itemStack.getItem()).canWrench(player, x, y, z);
            }

            /*
            if (itemStack.getItem() instanceof ItemScrewdriver) {
                return true;
            }
            */

            try {
                Class wrenchClass = itemStack.getItem().getClass();

                if ((wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench")) || (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))) {
                    return itemStack.getMetadata() < itemStack.getMaxDurability();
                }
            } catch (Exception e) {

            }
        }

        return false;
    }

    public static boolean damageWrench(EntityPlayer player, int x, int y, int z) {
        return damageWrench(player, player.getHeldItem(), x, y, z);
    }

    public static boolean damageWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z) {
        if (isUsableWrench(player, itemStack, x, y, z)) {
            if (itemStack.getItem() instanceof IToolWrench) {
                ((IToolWrench) itemStack.getItem()).wrenchUsed(player, x, y, z);

                return true;
            }

            try {
                Class wrenchClass = itemStack.getItem().getClass();

                if ((wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench")) || (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))) {
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
                    methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, player);

                    return true;
                }
            } catch (Exception e) {

            }
        }

        return false;
    }
}
