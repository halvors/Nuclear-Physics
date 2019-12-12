package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IProgressInfoHandler {
    double getProgress();
}