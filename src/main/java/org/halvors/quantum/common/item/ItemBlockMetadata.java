package org.halvors.quantum.common.item;

import net.minecraft.block.Block;

public class ItemBlockMetadata extends ItemBlock {
	protected ItemBlockMetadata(Block block) {
		super(block);

		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
}
