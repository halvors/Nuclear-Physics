package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

public class ItemArmorHazmat extends ItemArmorBase implements IAntiPoisonArmor {
    private static final ItemArmor.ArmorMaterial material = EnumHelper.addArmorMaterial("hazmat", 0, new int[] { 0, 0, 0, 0 }, 0);

    public ItemArmorHazmat(String name, int slot) {
        super(name, material, slot);

        setMaxDurability(200000);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return ResourceUtility.getResource(Resource.TEXTURE_MODELS, "hazmat.png").toString();
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
        return armorStack != null && compareStack != null && armorStack.getItem() == compareStack.getItem();
    }

    @Override
    public boolean areAllPartsNeeded(ItemStack armorStack, EntityLivingBase entity, DamageSource source, Object... args) {
        return true;
    }
}