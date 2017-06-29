package org.halvors.quantum.common.utility;

import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.common.ConfigurationManager.Integration;

public class WrenchUtility {
	/**
	 * Whether or not the player has a usable wrench for a block at the coordinates given.
	 * @param player - the player using the wrench
	 * @param x - the x coordinate of the block being wrenched
	 * @param y - the y coordinate of the block being wrenched
	 * @param z - the z coordinate of the block being wrenched
	 * @return if the player can use the wrench
	 */
	public static boolean hasUsableWrench(EntityPlayer player, int x, int y, int z) {
		ItemStack itemStack = player.getCurrentEquippedItem();

		if (itemStack != null) {
			Item item = itemStack.getItem();

			if (Integration.isCoFHCoreEnabled) {
				// Check if item is a CoFH wrench.
				/*
				if (item instanceof IToolHammer) {
					IToolHammer wrench = (IToolHammer) item;

					return wrench.isUsable(itemStack, player, x, y, z);
				}
				*/
			}

			if (Integration.isMekanismEnabled) {
				// Check if item is a Mekanism wrench.
				if (item instanceof IMekWrench) {
					IMekWrench wrench = (IMekWrench) item;

					return wrench.canUseWrench(player, x, y, z);
				}
			}
		}

		return false;
	}
}
