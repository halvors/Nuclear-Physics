package org.halvors.quantum.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;

import javax.annotation.Nonnull;

public class PotionRadiation extends CustomPotion {
    private static final PotionRadiation instance = new PotionRadiation(true, 0x4e9331, "radiation");

    public PotionRadiation(boolean isBadEffect, int color, String name) {
        super(isBadEffect, color, name);

        setIconIndex(6, 0);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (entity.getEntityWorld().rand.nextFloat() > 0.9D - amplifier * 0.07D) {
            entity.attackEntityFrom(PoisonRadiation.getDamageSource(), 1.0F);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    public static PotionRadiation getInstance() {
        return instance;
    }
}
