package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IArmorSet {
    EntityEquipmentSlot getArmorType();

    boolean isPartOfSet(ItemStack armorStack, ItemStack compareStack);

    boolean areAllPartsNeeded(ItemStack armorStack, EntityLivingBase entity, DamageSource source, Object... args);
}
