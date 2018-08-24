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
    public String getTranslationKey(final ItemStack itemStack) {
        return super.getUnlocalizedNameInefficiently(itemStack) + "." + itemStack.getMetadata();
    }
}
