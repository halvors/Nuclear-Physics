package org.halvors.quantum.client.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISimpleBlockRenderer {
    boolean renderItem(ItemStack itemStack);

    //boolean renderStatic(RenderBlocks renderer, IBlockAccess access, int x, int y, int z);
}
