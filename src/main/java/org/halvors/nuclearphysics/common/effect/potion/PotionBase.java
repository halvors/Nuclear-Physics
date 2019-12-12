package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.potion.Potion;
import org.halvors.nuclearphysics.NuclearPhysics;

public abstract class PotionBase extends Potion {
    public PotionBase(final boolean isBadEffect, final int color, final String name) {
        super(isBadEffect, color);

        setPotionName(this, name);
    }

    /**
     * Set the registry name of {@code potion} to {@code potionName} and the unlocalised name to the full registry name.
     *
     * @param potion The potion
     * @param potionName The potion's name
     */
    public static void setPotionName(final Potion potion, final String potionName) {
        potion.setRegistryName(NuclearPhysics.ID, potionName);
        potion.setPotionName("effect." + potionName);
    }
}
