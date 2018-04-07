package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.item.ItemArmor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

public class ItemArmorBase extends ItemArmor {
    protected final String name;

    public ItemArmorBase(String name, ArmorMaterial material, int slot) {
        super(material, 0, slot);

        this.name = name;

        setUnlocalizedName(Reference.ID + "." + name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }
}