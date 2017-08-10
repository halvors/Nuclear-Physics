package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

public class ItemArmorQuantum extends ItemArmor {
    protected final String name;

    public ItemArmorQuantum(String name, ArmorMaterial material, EntityEquipmentSlot slot) {
        super(material, 0, slot);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(Reference.ID, name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }

    public void registerItemModel() {
        NuclearPhysics.getProxy().registerItemRenderer(this, 0, name);
    }
}
