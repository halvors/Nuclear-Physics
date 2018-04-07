package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet.ArmorType;

import java.awt.*;
import java.util.EnumSet;

public abstract class PotionBase extends Potion {
    private static final EnumSet<ArmorType> armorRequired = EnumSet.of(ArmorType.HEAD, ArmorType.CHEST, ArmorType.LEGS, ArmorType.FEET);
    private final String name;

    public PotionBase(boolean isBadEffect, int red, int green, int blue, String name) {
        this(isBadEffect, new Color(red, green, blue).getRGB(), name);
    }

    public PotionBase(boolean isBadEffect, int liquidColor, String name) {
        super(21, isBadEffect, liquidColor); // Verify potion id?

        this.name = name;

        setPotionName("effect." + name);
    }

    public EnumSet<ArmorType> getArmorRequired() {
        return armorRequired;
    }

    public boolean isEntityProtected(int x, int y, int z, EntityLivingBase entity, int amplifier) {
        EnumSet<ArmorType> armorWorn = EnumSet.noneOf(ArmorType.class);

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;

            for (ItemStack itemStack : entityPlayer.inventory.armorInventory) {
                if (itemStack != null) {
                    if ((itemStack.getItem() instanceof IAntiPoisonArmor)) {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor) itemStack.getItem();

                        if (armor.isProtectedFromPoison(itemStack, entity, name)) {
                            armorWorn.add(ArmorType.values()[(armor.getArmorType() % ArmorType.values().length)]);

                            armor.onProtectFromPoison(itemStack, entity, name);
                        }
                    }
                }
            }
        }

        return armorWorn.containsAll(armorRequired);
    }

    public void poisonEntity(int x, int y, int z, EntityLivingBase entity) {
        poisonEntity(x, y, z, entity, 0);
    }

    public void poisonEntity(int x, int y, int z, EntityLivingBase entity, int amplifier) {
        if (!isEntityProtected(x, y, z, entity, amplifier)) {
            doEntityPoisoning(x, y, z, entity, amplifier);
        }
    }

    protected abstract void doEntityPoisoning(int x, int y, int z, EntityLivingBase entity, int amplifier);
}
