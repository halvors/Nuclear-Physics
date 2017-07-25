package org.halvors.quantum.common.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.Reference;

public class ItemArmorQuantum extends ItemArmor {
    public ItemArmorQuantum(String name, ArmorMaterial material, EntityEquipmentSlot slot) {
        super(material, 0, slot);

        setUnlocalizedName(name);
        setRegistryName(Reference.ID, name);
        setCreativeTab(Quantum.getCreativeTab());
    }
}
