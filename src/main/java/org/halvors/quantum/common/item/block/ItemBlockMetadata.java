package org.halvors.quantum.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.utility.LanguageUtility;

import javax.annotation.Nonnull;

public class ItemBlockMetadata extends ItemBlockTooltip {
	public ItemBlockMetadata(Block block) {
		super(block);

		setHasSubtypes(true);
		setNoRepair();
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack itemStack) {
		return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
	}
}
