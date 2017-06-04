package org.halvors.quantum.common.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import org.halvors.quantum.Quantum;

/**
 * This is a basic Block that is meant to be extended by other Blocks.
 *
 * @author halvors
 */
public abstract class Block extends BlockContainer {
	protected final String name;

	protected Block(String name, Material material) {
		super(material);

		this.name = name;

		setUnlocalizedName(name);
		setCreativeTab(Quantum.getCreativeTab());
	}
}