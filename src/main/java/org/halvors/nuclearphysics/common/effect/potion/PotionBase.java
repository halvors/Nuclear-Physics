package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.potion.Potion;
import org.halvors.nuclearphysics.common.Reference;

public abstract class PotionBase extends Potion {
    public PotionBase(boolean isBadEffect, int color, String name) {
        super(isBadEffect, color);

        setPotionName(this, name);
    }

    /**
     * Set the registry name of {@code potion} to {@code potionName} and the unlocalised name to the full registry name.
     *
     * @param potion The potion
     * @param potionName The potion's name
     */
    public static void setPotionName(Potion potion, String potionName) {
        potion.setRegistryName(Reference.ID, potionName);
        potion.setPotionName("effect." + potionName);
    }
}
