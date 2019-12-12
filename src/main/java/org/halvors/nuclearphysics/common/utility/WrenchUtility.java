package org.halvors.nuclearphysics.common.utility;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.item.IWrench;

public class WrenchUtility {
    /**
     * Whether or not the player has a usable wrench for a block at the coordinates given.
     * @param player - the player using the wrench
     * @param pos - the coordinate of the block being wrenched
     * @return if the player can use the wrench
     */
    public static boolean hasUsableWrench(final PlayerEntity player, final EnumHand hand, final BlockPos pos) {
        final ItemStack itemStack = player.getHeldItemMainhand();

        if (!itemStack.isEmpty()) {
            final Item item = itemStack.getItem();

            if (item instanceof IWrench) {
                return ((IWrench) item).canUseWrench(itemStack, player, pos);
            /*
            } else if (Integration.isMekanismLoaded && item instanceof IMekWrench) {
                return ((IMekWrench) item).canUseWrench(itemStack, player, pos);
            } else if (Integration.isBuildcraftLoaded && item instanceof IToolWrench) {
                return ((IToolWrench) item).canWrench(player, hand, itemStack, null);
            } else if (Integration.isCOFHCoreLoaded && item instanceof IToolHammer) {
                return ((IToolHammer) item).isUsable(itemStack, player, pos);
            */
            }
        }

        return false;
    }
}
