package org.halvors.nuclearphysics.common.effect.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;

import java.util.EnumSet;

public abstract class Poison {
    private static final EnumSet<EntityEquipmentSlot> armorRequired = EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    private final String name;

    public Poison(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EnumSet<EntityEquipmentSlot> getArmorRequired() {
        return armorRequired;
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier) {
        if (!isEntityProtected(pos, entity, amplifier)) {
            doPoisonEntity(pos, entity, amplifier);
        }
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity) {
        poisonEntity(pos, entity, 0);
    }

    public boolean isEntityProtected(BlockPos pos, EntityLivingBase entity, int amplifier) {
        EnumSet<EntityEquipmentSlot> armorWorn = EnumSet.noneOf(EntityEquipmentSlot.class);

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;

            for (int i = 0; i < entityPlayer.inventory.armorInventory.length; i++) {
                ItemStack itemStack = entityPlayer.inventory.armorInventory[i];

                if (itemStack != null) {
                    if ((itemStack.getItem() instanceof IAntiPoisonArmor)) {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor) itemStack.getItem();

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

    protected abstract void doPoisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier);
}