package com.calclavia.core.lib.prefab.damage;

import net.minecraft.util.DamageSource;

public class ElectricalDamage extends CustomDamageSource
{
    public ElectricalDamage(DamageSource source)
    {
        super("electrocution");
        setDamageBypassesArmor();
        setDifficultyScaled();
    }
}
