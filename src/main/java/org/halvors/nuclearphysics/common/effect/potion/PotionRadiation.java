package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.effect.damage.CustomDamageSource;
import org.halvors.nuclearphysics.common.init.ModPotions;

import javax.annotation.Nonnull;

public class PotionRadiation extends PotionBase {
    private static final CustomDamageSource damageSource = new CustomDamageSource("radiation").setDamageBypassesArmor();

    public PotionRadiation() {
        super(true, 78, 147, 49, "radiation");

        setIconIndex(6, 0);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (entity.getEntityWorld().rand.nextFloat() > 0.9D - amplifier * 0.07D) {
            entity.attackEntityFrom(damageSource, 1.0F);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    protected void applyPoisonToEntity(BlockPos pos, EntityLivingBase entity, int amplifier) {
        if (ConfigurationManager.General.enableRadiationRoisoning) {
            entity.addPotionEffect(new PotionEffect(ModPotions.potionRadiation, 300 * (amplifier + 1), amplifier));
        }
    }

    public static CustomDamageSource getDamageSource() {
        return damageSource;
    }
}
