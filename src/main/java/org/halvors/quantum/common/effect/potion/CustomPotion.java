package org.halvors.quantum.common.effect.potion;

import net.minecraft.potion.Potion;

public abstract class CustomPotion extends Potion {
    public CustomPotion(boolean isBadEffect, int color, String name) {
        super(isBadEffect, color);

        setPotionName("potion." + name);
    }

    @Override
    public Potion setIconIndex(int x, int y) {
        super.setIconIndex(x, y);

        return this;
    }

    @Override
    protected Potion setEffectiveness(double effectiveness) {
        super.setEffectiveness(effectiveness);

        return this;
    }
}
