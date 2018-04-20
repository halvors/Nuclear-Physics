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

    EnumSet<EntityEquipmentSlot> getArmorPartsRequired();
}
