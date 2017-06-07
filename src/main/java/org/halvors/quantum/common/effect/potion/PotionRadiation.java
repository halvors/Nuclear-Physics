package org.halvors.quantum.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;

public class PotionRadiation extends CustomPotion {
    public static final PotionRadiation INSTANCE = new PotionRadiation(21, true, 5149489, "radiation");

    public PotionRadiation(int id, boolean isBadEffect, int color, String name) {
        super(id, isBadEffect, color, name);

        setIconIndex(6, 0);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (entity.worldObj.rand.nextFloat() > 0.9D - amplifier * 0.07D) {
            entity.attackEntityFrom(PoisonRadiation.damageSource, 1.0F);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
