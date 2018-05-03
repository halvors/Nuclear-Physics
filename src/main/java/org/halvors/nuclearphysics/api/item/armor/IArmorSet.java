package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public interface IArmorSet {
    EntityEquipmentSlot getEquipmentSlot();

    boolean isArmorPartOfSet(ItemStack itemStack);

    default EnumSet<EntityEquipmentSlot> getArmorPartsRequired() {
        return EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    }
}
