package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.common.Reference;

import javax.annotation.Nonnull;

public class ItemArmorHazmat extends ItemArmorBase implements IAntiPoisonArmor {
    private static final ArmorMaterial material = EnumHelper.addArmorMaterial("hazmat" , "hazmat", 0, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 0);

    public ItemArmorHazmat(final String name, final EntityEquipmentSlot slot) {
        super(name, material, slot);

        setMaxDamage(200000);
    }

    @Override
    @Nonnull
    public String getArmorTexture(final ItemStack itemStack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
        return Reference.PREFIX + "textures/models/hazmat.png";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isProtectedFromPoison(final ItemStack itemStack, final EntityLivingBase entity, final PoisonType type) {
        switch (type) {
            case RADIATION:
            case CHEMICAL:
            case CONTAGIOUS:
                return true;
        }

        return false;
    }

    @Override
    public void onProtectFromPoison(final ItemStack itemStack, final EntityLivingBase entity, final PoisonType type) {
        itemStack.damageItem(1, entity);
    }
}


