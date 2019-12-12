package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public interface IArmorSet {
    EquipmentSlotType getArmorType();

    boolean isArmorPartOfSet(ItemStack itemStack);

    default EnumSet<EquipmentSlotType> getArmorPartsRequired() {
        return EnumSet.of(EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET);
    }
}
