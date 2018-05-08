package org.halvors.nuclearphysics.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockMetadata extends ItemBlockTooltip {
	public ItemBlockMetadata(Block block) {
		super(block);

		setMaxDurability(0);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(final ItemStack itemStack) {
		return super.getUnlocalizedName(itemStack) + "." + itemStack.getMetadata();
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
}
