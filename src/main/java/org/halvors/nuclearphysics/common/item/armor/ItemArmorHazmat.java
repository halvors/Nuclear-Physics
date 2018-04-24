package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.common.Reference;

public class ItemArmorHazmat extends ItemArmorBase implements IAntiPoisonArmor {
    private static final ItemArmor.ArmorMaterial material = EnumHelper.addArmorMaterial("hazmat", 0, new int[] { 0, 0, 0, 0 }, 0);

    public ItemArmorHazmat(String name, EntityEquipmentSlot slot) {
        super(name, material, slot);

        setMaxDurability(200000);
    }

    @Override
    public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
        return Reference.PREFIX + "textures/models/hazmat.png";
    }
}
