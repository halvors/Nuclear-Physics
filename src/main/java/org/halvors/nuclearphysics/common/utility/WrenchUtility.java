package org.halvors.nuclearphysics.common.utility;

import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.item.IWrench;
import org.halvors.nuclearphysics.common.Integration;

public class WrenchUtility {
    /**
     * Whether or not the player has a usable wrench for a block at the coordinates given.
     * @param player - the player using the wrench
     * @param x, y, z - the coordinate of the block being wrenched
     * @return if the player can use the wrench
     */
    public static boolean hasUsableWrench(EntityPlayer player, int x, int y, int z) {
        ItemStack itemStack = player.getHeldItem();

        if (itemStack != null) {
            Item item = itemStack.getItem();

            if (item instanceof IWrench) {
                IWrench wrench = (IWrench) item;

                return wrench.canUseWrench(itemStack, player, x, y, z);
            } else if (Integration.isMekanismLoaded) {
                if (item instanceof IMekWrench) {
                    IMekWrench wrench = (IMekWrench) item;

                    return wrench.canUseWrench(player, x, y, z);
                }
            }

            /*
            } else if (ConfigurationManager.Integration.isBuildcraftEnabled) {
                if (item instanceof IToolWrench) {
                    IToolWrench wrench = (IToolWrench) item;

                    return wrench.canWrench(player, hand, itemStack, null);
                }
            } else if (ConfigurationManager.Integration.isCoFHEnabled) {
                if (item instanceof IToolHammer) {
                    IToolHammer wrench = (IToolHammer) item;

                    return wrench.isUsable(itemStack, player, pos);
                }

            */
        }

        return false;
    }
}
