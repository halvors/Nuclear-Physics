package com.calclavia.core.lib.prefab.potion;

import com.calclavia.core.lib.Calclavia;
import net.minecraft.potion.Potion;

public abstract class CustomPotion extends Potion
{
    public CustomPotion(int id, boolean isBadEffect, int color, String name)
    {
        super(getPotionID(name, id), isBadEffect, color);
        setPotionName("potion." + name);
        Potion.potionTypes[getId()] = this;
    }

    public static int getPotionID(String name, int id)
    {
        Calclavia.CONFIGURATION.load();
        int finalID = Calclavia.CONFIGURATION.get("Potion ID", name + " ID", id).getInt(id);
        Calclavia.CONFIGURATION.save();
        return finalID;
    }

    public Potion setIconIndex(int par1, int par2) {
        super.setIconIndex(par1, par2);

        return this;
    }

    protected Potion setEffectiveness(double par1)
    {
        super.setEffectiveness(par1);
        return this;
    }
}
