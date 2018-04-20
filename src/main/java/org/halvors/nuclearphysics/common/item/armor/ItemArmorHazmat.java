package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet;
import org.halvors.nuclearphysics.common.Reference;

import javax.annotation.Nonnull;

public class ItemArmorHazmat extends ItemArmorBase implements IAntiPoisonArmor {
    private static final ArmorMaterial material = EnumHelper.addArmorMaterial("hazmat" , "hazmat", 0, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public ItemArmorHazmat(String name, EntityEquipmentSlot slot) {
        super(name, material, slot);

        setMaxDamage(200000);
    }

    @Override
    @Nonnull
    public String getArmorTexture(ItemStack itemStack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.PREFIX + "textures/models/hazmat.png";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type) {
        switch (type) {
            case RADIATION:
            case CHEMICAL:
            case CONTAGIOUS:
                return true;
        }

        return false;
    }

    @Override
    public void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entity, PoisonType type) {
        itemStack.damageItem(1, entity);
    }
}


