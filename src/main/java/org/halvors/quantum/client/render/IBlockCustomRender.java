package org.halvors.quantum.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IBlockCustomRender {
    ISimpleBlockRenderer getRenderer();
}
