package org.halvors.quantum.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.utility.LanguageUtility;

public class ItemBlockMetadata extends ItemBlockQuantum {
	public ItemBlockMetadata(Block block) {
		super(block);

		setHasSubtypes(true);
		setNoRepair();
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
	}

	// TODO: Only add index to name if there is more than to items of that very same block.
	/*
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String localized = LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata() + ".name");

		if (localized != null && !localized.isEmpty()) {
			return getUnlocalizedName() + "." + itemStack.getMetadata();
		}

		return getUnlocalizedName();
	}
	*/
}
