package org.halvors.quantum.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public interface ISimpleItemRenderer {
    void renderInventoryItem(ItemStack itemStack);
}
