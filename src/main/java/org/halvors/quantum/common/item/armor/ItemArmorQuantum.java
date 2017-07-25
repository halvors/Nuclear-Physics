package org.halvors.quantum.common.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.Reference;

public class ItemArmorQuantum extends ItemArmor {
    protected final String name;

    public ItemArmorQuantum(String name, ArmorMaterial material, EntityEquipmentSlot slot) {
        super(material, 0, slot);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(Reference.ID, name);
        setCreativeTab(Quantum.getCreativeTab());
    }

    public void registerItemModel() {
        Quantum.getProxy().registerItemRenderer(this, 0, name);
    }
}
