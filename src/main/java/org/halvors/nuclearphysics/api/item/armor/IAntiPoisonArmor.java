package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;

public interface IAntiPoisonArmor extends IArmorSet {
    default boolean isProtectedFromPoison(final ItemStack itemStack, final EntityLivingBase entity, final EnumPoisonType type) {
        switch (type) {
            case RADIATION:
            case CHEMICAL:
            case CONTAGIOUS:
                return true;
        }

        return false;
    }

    default void onProtectFromPoison(final ItemStack itemStack, final EntityLivingBase entity, final EnumPoisonType type) {
        itemStack.damageItem(1, entity);
    }
}

