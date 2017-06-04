package org.halvors.quantum.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.poison.IAntiPoisonArmor;

public class ItemArmorHazmat extends ItemArmor implements IAntiPoisonArmor {
    public static final ItemArmor.ArmorMaterial hazmatArmorMaterial = EnumHelper.addArmorMaterial("HAZMAT", 0, new int[] { 0, 0, 0, 0 }, 0);

    public ItemArmorHazmat(String name, int slot) {
        super(hazmatArmorMaterial, 0, slot);

        setUnlocalizedName(Reference.PREFIX + name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(Quantum.getCreativeTab());
        setMaxDurability(200000);
    }

    @Override
    public String getArmorTexture(ItemStack itemStack, Entity entity, int slot, String type) {
        return Reference.PREFIX + "models/hazmat.png";
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
    public int getArmorType() {
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


