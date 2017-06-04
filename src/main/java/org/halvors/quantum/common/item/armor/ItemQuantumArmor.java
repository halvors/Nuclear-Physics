package org.halvors.quantum.common.item.armor;

import net.minecraft.item.ItemArmor;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;

public class ItemQuantumArmor extends ItemArmor {
    public ItemQuantumArmor(String name, ArmorMaterial armorMaterial, int slot) {
        super(armorMaterial, 0, slot);

        setUnlocalizedName(name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(Quantum.getCreativeTab());
    }
}
