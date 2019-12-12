package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public interface IInfoHandler {
    List<String> getInfo();
}
