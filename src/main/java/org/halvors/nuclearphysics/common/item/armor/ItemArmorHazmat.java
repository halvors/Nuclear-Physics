package org.halvors.nuclearphysics.common.item.armor;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.common.Reference;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemArmorHazmat extends ArmorItem implements IAntiPoisonArmor {
    public static final HazmatMaterial HAZMAT_MATERIAL = new HazmatMaterial();

    public ItemArmorHazmat(final String name, final Properties properties, final EquipmentSlotType slot) {
        super(HAZMAT_MATERIAL, EquipmentSlotType.CHEST, properties.setNoRepair());

        //setMaxDamage(200000);
    }

    @Override
    @Nonnull
    public String getArmorTexture(final ItemStack itemStack, final Entity entity, final EquipmentSlotType slot, final String type) {
        return Reference.PREFIX + "textures/models/hazmat.png";
    }

    @Override
    public EquipmentSlotType getArmorType() {
        return slot;
    }

    @Override
    public boolean isArmorPartOfSet(ItemStack itemStack) {
        return false;
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    protected static class HazmatMaterial implements IArmorMaterial {
        @Override
        public int getDurability(EquipmentSlotType slotType) {
            return 200000;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotType) {
            return 0;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY;
        }

        @Override
        public String getName() {
            return "hazmat";
        }

        @Override
        public float getToughness() {
            return 0;
        }
    }
}


