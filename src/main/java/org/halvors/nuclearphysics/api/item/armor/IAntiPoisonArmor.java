package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;

public interface IAntiPoisonArmor extends IArmorSet {
    boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type);

    void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type);
}

