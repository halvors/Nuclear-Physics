package com.calclavia.core.lib.prefab.potion;

import com.calclavia.core.lib.prefab.poison.PoisonRadiation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PotionRadiation extends CustomPotion
{
    public static final PotionRadiation INSTANCE = new PotionRadiation(21, true, 5149489, "radiation");

    public PotionRadiation(int id, boolean isBadEffect, int color, String name)
    {
        super(id, isBadEffect, color, name);
        setIconIndex(6, 0);
    }

    public void performEffect(EntityLivingBase par1EntityLiving, int amplifier)
    {
        if (par1EntityLiving.worldObj.rand.nextFloat() > 0.9D - amplifier * 0.07D)
        {
            par1EntityLiving.attackEntityFrom(PoisonRadiation.damageSource, 1.0F);
            if ((par1EntityLiving instanceof EntityPlayer)) {
                ((EntityPlayer)par1EntityLiving).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    public boolean isReady(int duration, int amplifier)
    {
        if (duration % 10 == 0) {
            return true;
        }
        return false;
    }
}
