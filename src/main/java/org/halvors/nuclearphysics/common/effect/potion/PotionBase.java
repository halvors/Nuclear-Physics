package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.potion.Potion;

public abstract class PotionBase extends Potion {
    public PotionBase(boolean isBadEffect, int color, String name) {
        super(21, isBadEffect, color); // Verify potion id?

        setPotionName("effect." + name);
    }
}
