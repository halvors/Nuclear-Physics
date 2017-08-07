package org.halvors.quantum.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabQuantum extends CreativeTabs {
	public CreativeTabQuantum() {
		super(Reference.ID);
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Nonnull
	public ItemStack getTabIconItem() {
		return new ItemStack(Item.getItemFromBlock(QuantumBlocks.blockReactorCell));
	}
}