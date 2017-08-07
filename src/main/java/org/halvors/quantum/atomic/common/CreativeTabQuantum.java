package org.halvors.quantum.atomic.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.common.QuantumBlocks;
import org.halvors.quantum.atomic.common.Reference;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabQuantum extends CreativeTabs {
	public CreativeTabQuantum() {
		super(Reference.ID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return Item.getItemFromBlock(QuantumBlocks.blockReactorCell);
	}
}