package com.calclavia.core.lib.prefab.poison;

import com.calclavia.core.lib.prefab.potion.PotionRadiation;
import net.minecraft.entity.EntityLivingBase;
import com.calclavia.core.lib.prefab.damage.CustomDamageSource;
import com.calclavia.core.lib.prefab.potion.CustomPotionEffect;
import com.calclavia.core.lib.transform.vector.Vector3;

public class PoisonRadiation extends Poison
{
    public static final Poison INSTANCE = new PoisonRadiation("radiation");
    public static final CustomDamageSource damageSource = new CustomDamageSource("radiation").setDamageBypassesArmor();
    public static boolean disabled = false;

    public PoisonRadiation(String name)
    {
        super(name);
    }

    public boolean isEntityProtected(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
    {
        return (emitPosition != null) && (getAntiPoisonBlockCount(entity.worldObj, emitPosition, new Vector3(entity)) <= amplifier) && (super.isEntityProtected(emitPosition, entity, amplifier));
    }

    protected void doPoisonEntity(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
    {
        if (!disabled) {
            entity.addPotionEffect(new CustomPotionEffect(PotionRadiation.INSTANCE.getId(), 300 * (amplifier + 1), amplifier, null));
        }
    }
}
