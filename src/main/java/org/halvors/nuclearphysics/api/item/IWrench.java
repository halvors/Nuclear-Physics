package org.halvors.nuclearphysics.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public interface IWrench {
    boolean canUseWrench(ItemStack itemStack, PlayerEntity player, BlockPos pos);
}
