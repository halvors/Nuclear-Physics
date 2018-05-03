package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMetadata extends ItemBlockTooltip {
	public ItemBlockMetadata(Block block) {
		super(block);

		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(final ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack) + "." + itemStack.getMetadata();
	}
}
