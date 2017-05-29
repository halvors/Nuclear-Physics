package com.calclavia.core.api.atomicscience.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract interface IAntiPoisonArmor  {
    public abstract boolean isProtectedFromPoison(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase, String paramString);

    public abstract void onProtectFromPoison(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase, String paramString);

    public abstract int getArmorType();
}
