package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMetadata extends ItemBlockTooltip {
	public ItemBlockMetadata(Block block) {
		super(block);

		setMaxDurability(0);
		setHasSubtypes(true);
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack) + "." + itemStack.getMetadata();
	}
}
