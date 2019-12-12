package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;

import java.util.function.Consumer;

public interface IAntiPoisonArmor extends IArmorSet {
    default boolean isProtectedFromPoison(final ItemStack itemStack, final LivingEntity entity, final EnumPoisonType type) {
        switch (type) {
            case RADIATION:
            case CHEMICAL:
            case CONTAGIOUS:
                return true;
        }

        return false;
    }

    default void onProtectFromPoison(final ItemStack itemStack, final LivingEntity entity, final EnumPoisonType type) {
        if (entity instanceof PlayerEntity) {
            itemStack.damageItem(1, entity, new Consumer<LivingEntity>() {
                @Override
                public void accept(LivingEntity livingEntity) {

                }
            });
        }
    }
}

