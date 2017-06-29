package org.halvors.quantum.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public interface ISimpleBlockRenderer {
    boolean renderItem(ItemStack itemStack);

    boolean renderStatic(RenderBlocks renderer, IBlockAccess access, int x, int y, int z);
}
