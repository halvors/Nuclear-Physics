package org.halvors.quantum.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IBlockCustomRender {
    ISimpleBlockRenderer getRenderer();
}
