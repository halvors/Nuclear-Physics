package org.halvors.nuclearphysics.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IWrench {
    boolean canUseWrench(ItemStack itemStack, EntityPlayer player, int x, int y, int z);
}
