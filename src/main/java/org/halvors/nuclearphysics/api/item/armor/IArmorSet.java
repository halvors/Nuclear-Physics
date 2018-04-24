package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public interface IArmorSet {
    enum EntityEquipmentSlot {
        HEAD,
        CHEST,
        LEGS,
        FEET
    }

    EntityEquipmentSlot getEquipmentSlot();

    boolean isArmorPartOfSet(ItemStack itemStack);

    default EnumSet<EntityEquipmentSlot> getArmorPartsRequired() {
        return EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    }
}
