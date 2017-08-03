package org.halvors.quantum.common.creative;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.Reference;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabQuantumComponents extends CreativeTabs {
	public CreativeTabQuantumComponents() {
		super(Reference.ID + " Components");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return QuantumItems.itemMotor;
	}
}