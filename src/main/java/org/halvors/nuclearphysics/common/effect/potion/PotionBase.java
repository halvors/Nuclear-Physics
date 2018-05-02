package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.potion.Potion;

public abstract class PotionBase extends Potion {
    public PotionBase(final boolean isBadEffect, final int color, final String name) {
        super(21, isBadEffect, color); // Verify potion id?

        setPotionName("effect." + name);
    }
}
