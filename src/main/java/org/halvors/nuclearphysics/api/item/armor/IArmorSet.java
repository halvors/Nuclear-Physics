package org.halvors.nuclearphysics.api.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.EnumSet;

public interface IArmorSet {
    EntityEquipmentSlot getEquipmentSlot();

    boolean isArmorPartOfSet(ItemStack itemStack);

    EnumSet<EntityEquipmentSlot> getArmorPartsRequired();
}
