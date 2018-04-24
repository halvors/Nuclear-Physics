package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;

public interface IAntiPoisonArmor extends IArmorSet {
    default boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type) {
        switch (type) {
            case RADIATION:
            case CHEMICAL:
            case CONTAGIOUS:
                return true;
        }

        return false;
    }

    default void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type) {
        itemStack.damageItem(1, entity);
    }
}

