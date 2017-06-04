package org.halvors.quantum.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.base.MachineType;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTab extends CreativeTabs {
	public CreativeTab() {
		super("tab" + Reference.NAME);
	}

	@Override
	public ItemStack getIconItemStack() {
		return MachineType.BASIC_ELECTRICITY_METER.getItemStack();
	}

	@Override
	public Item getTabIconItem() {
		return MachineType.BASIC_ELECTRICITY_METER.getItem();
	}
}