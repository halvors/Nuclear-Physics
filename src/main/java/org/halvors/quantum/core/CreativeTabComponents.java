package org.halvors.quantum.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.common.Reference;
import org.halvors.quantum.core.init.ComponentItems;

/**
 * This is a custom creative tab used only by this mod.
 *
 * @author halvors
 */
public class CreativeTabComponents extends CreativeTabs {
	public CreativeTabComponents() {
		super(Reference.ID + " Components");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return ComponentItems.itemMotor;
	}
}