package org.halvors.nuclearphysics.api.effect.poison;

import net.minecraft.entity.LivingEntity;

public interface IPoison {
    boolean isEntityProtected(final LivingEntity entity, int amplifier);

    void poisonEntity(final LivingEntity entity, int amplifier);
}
