package org.halvors.quantum.lib.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.transform.vector.Vector3;

@SideOnly(Side.CLIENT)
public class TileRender {
    public boolean renderStatic(RenderBlocks renderer, Vector3 position) {
        return false;
    }

    public boolean renderDynamic(Vector3 position, boolean isItem, float frame) {
        return false;
    }

    public boolean renderItem(ItemStack itemStack) {
        return false;
    }
}
