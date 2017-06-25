package org.halvors.quantum.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabQuantum extends CreativeTabs {
	public CreativeTabQuantum() {
		super("tab" + Reference.NAME);
	}

	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(Quantum.blockReactorCell);
	}

	@Override
	public Item getTabIconItem() {
		return new ItemBlock(Quantum.blockReactorCell);
	}
}