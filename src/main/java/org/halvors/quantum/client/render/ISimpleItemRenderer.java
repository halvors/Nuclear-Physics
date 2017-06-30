package org.halvors.quantum.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISimpleItemRenderer {
    void renderInventoryItem(ItemStack itemStack);
}
