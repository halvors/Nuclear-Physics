package org.halvors.nuclearphysics.common.effect.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModPotions;

import javax.annotation.Nonnull;

public class PoisonRadiation extends PoisonBase {
    public PoisonRadiation() {
        super(true, 78, 147, 49, EnumPoisonType.RADIATION);

        setIconIndex(6, 0);
    }

    @Override
    public void performEffect(final @Nonnull EntityLivingBase entity, final int amplifier) {
        final World world = entity.worldObj;

        if (world.rand.nextFloat() > 0.9 - amplifier * 0.07) {
            entity.attackEntityFrom(damageSource, 1);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    @Override
    public boolean isReady(final int duration, final int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void performPoisonEffect(final EntityLivingBase entity, final int amplifier) {
        if (General.enableRadiationRoisoning) {
            entity.addPotionEffect(new PotionEffect(ModPotions.poisonRadiation.getId(), 300 * (amplifier + 1), amplifier));
        }
    }
}