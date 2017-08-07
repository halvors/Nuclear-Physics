package org.halvors.quantum.atomic.common.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.quantum.api.item.armor.IAntiPoisonArmor;
import org.halvors.quantum.atomic.common.Reference;

public class ItemArmorHazmat extends ItemArmorQuantum implements IAntiPoisonArmor {
    private static final ArmorMaterial material = EnumHelper.addArmorMaterial("hazmat" , "hazmat", 0, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public ItemArmorHazmat(String name, EntityEquipmentSlot slot) {
        super(name, material, slot);

        setMaxDamage(200000);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.PREFIX + "textures/models/hazmat.png";
    }

    @Override
    public boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entity, String type) {
        return type.equalsIgnoreCase("radiation") || type.equalsIgnoreCase("chemical") || type.equalsIgnoreCase("contagious");
    }

    @Override
    public void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entity, String type) {
        itemStack.damageItem(1, entity);
    }

    @Override
    public EntityEquipmentSlot getArmorType() {
        return armorType;
    }

    @Override
    public boolean isPartOfSet(ItemStack armorStack, ItemStack compareStack) {
        if (armorStack != null && compareStack != null) {
            return armorStack.getItem() == compareStack.getItem();
        }

        return false;
    }

    @Override
    public boolean areAllPartsNeeded(ItemStack armorStack, EntityLivingBase entity, DamageSource source, Object... args) {
        return true;
    }
}


