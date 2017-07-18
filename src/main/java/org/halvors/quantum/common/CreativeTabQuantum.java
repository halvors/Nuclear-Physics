package org.halvors.quantum.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabQuantum extends CreativeTabs {
	public CreativeTabQuantum() {
		super("tab" + Reference.NAME);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock(QuantumBlocks.blockReactorCell);
	}
}