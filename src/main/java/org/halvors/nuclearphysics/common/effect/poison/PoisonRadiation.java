package org.halvors.nuclearphysics.common.effect.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.effect.damage.CustomDamageSource;
import org.halvors.nuclearphysics.common.effect.potion.CustomPotionEffect;
import org.halvors.nuclearphysics.common.effect.potion.PotionRadiation;

public class PoisonRadiation extends Poison {
    private static final PoisonRadiation instance = new PoisonRadiation("radiation");
    private static final CustomDamageSource damageSource = new CustomDamageSource("radiation").setDamageBypassesArmor();

    public PoisonRadiation(String name) {
        super(name);
    }

    @Override
    public boolean isEntityProtected(BlockPos pos, EntityLivingBase entity, int amplifier) {
        return pos != null && super.isEntityProtected(pos, entity, amplifier);
    }

    @Override
    protected void doPoisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier) {
        if (General.enableRadiationRoisoning) {
            entity.addPotionEffect(new CustomPotionEffect(PotionRadiation.getInstance(), 300 * (amplifier + 1), amplifier, null));
        }
    }

    public static PoisonRadiation getInstance() {
        return instance;
    }

    public static CustomDamageSource getDamageSource() {
        return damageSource;
    }
}
