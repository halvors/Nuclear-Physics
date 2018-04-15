package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.item.armor.IRadiationArmor;
import org.halvors.nuclearphysics.common.Reference;

import java.awt.*;
import java.util.EnumSet;

public abstract class PotionBase extends Potion {
    private static final EnumSet<EntityEquipmentSlot> armorRequired = EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    private final String name;

    public PotionBase(boolean isBadEffect, int red, int green, int blue, String name) {
        this(isBadEffect, new Color(red, green, blue).getRGB(), name);
    }

    public PotionBase(boolean isBadEffect, int liquidColor, String name) {
        super(isBadEffect, liquidColor);

        this.name = name;

        setPotionName(this, name);
    }

    /**
     * Set the registry name of {@code potion} to {@code potionName} and the unlocalised name to the full registry name.
     *
     * @param potion The potion
     * @param potionName The potion's name
     */
    public static void setPotionName(Potion potion, String potionName) {
        potion.setRegistryName(Reference.ID, potionName);
        potion.setPotionName("effect." + potionName);
    }

    public EnumSet<EntityEquipmentSlot> getArmorRequired() {
        return armorRequired;
    }

    public boolean isEntityProtected(BlockPos pos, EntityLivingBase entity, int amplifier) {
        EnumSet<EntityEquipmentSlot> armorWorn = EnumSet.noneOf(EntityEquipmentSlot.class);

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            for (ItemStack itemStack : player.getArmorInventoryList()) {
                if (itemStack != null) {
                    if ((itemStack.getItem() instanceof IRadiationArmor)) {
                        IRadiationArmor armor = (IRadiationArmor) itemStack.getItem();

                        if (armor.isProtectedFromPoison(itemStack, entity, name)) {
                            armorWorn.add(EntityEquipmentSlot.values()[(armor.getArmorType().ordinal() % EntityEquipmentSlot.values().length)]);

                            armor.onProtectFromPoison(itemStack, entity, name);
                        }
                    }
                }
            }
        }

        return armorWorn.containsAll(armorRequired);
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity) {
        poisonEntity(pos, entity, 0);
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier) {
        if (!isEntityProtected(pos, entity, amplifier)) {
            doEntityPoisoning(pos, entity, amplifier);
        }
    }

    protected abstract void doEntityPoisoning(BlockPos pos, EntityLivingBase entity, int amplifier);
}
