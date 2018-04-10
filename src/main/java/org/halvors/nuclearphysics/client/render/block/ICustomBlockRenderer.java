package org.halvors.nuclearphysics.client.render.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ICustomBlockRenderer {
    ISimpleBlockRenderer getRenderer();
}