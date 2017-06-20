package org.halvors.quantum.client.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public interface IBlockRenderer {
    boolean renderItem(ItemStack itemStack);

    boolean renderStatic(RenderBlocks renderer, IBlockAccess access, int x, int y, int z);
}
