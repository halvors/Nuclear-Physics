package org.halvors.quantum.common.item;

import net.minecraft.block.Block;
import org.halvors.quantum.Quantum;

/**
 * This is a basic ItemBlock that is meant to be extended by other ItemBlocks.
 *
 * @author halvors
 */
public class ItemBlock extends net.minecraft.item.ItemBlock {
	protected ItemBlock(Block block) {
		super(block);

		setCreativeTab(Quantum.getCreativeTab());
	}
}
