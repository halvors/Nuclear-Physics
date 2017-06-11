package org.halvors.quantum.common.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IAntiPoisonArmor extends IArmorSet {
    boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entity, String type);

    void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entity, String type);
}
