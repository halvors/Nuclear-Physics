package org.halvors.quantum.common.effect.potion;

import net.minecraft.potion.Potion;

public abstract class CustomPotion extends Potion {
    public CustomPotion(int id, boolean isBadEffect, int color, String name) {
        super(getPotionID(name, id), isBadEffect, color);

        setPotionName("potion." + name);
        Potion.potionTypes[getId()] = this;
    }

    private static int getPotionID(String name, int id) {
        return 21;

        /* TODO: Look over this and add to configuration.
        Calclavia.CONFIGURATION.load();
        int finalID = Calclavia.CONFIGURATION.get("Potion ID", name + " ID", id).getInt(id);
        Calclavia.CONFIGURATION.save();
        return finalID;
        */
    }

    @Override
    public Potion setIconIndex(int par1, int par2) {
        super.setIconIndex(par1, par2);

        return this;
    }

    @Override
    protected Potion setEffectiveness(double par1) {
        super.setEffectiveness(par1);

        return this;
    }
}
