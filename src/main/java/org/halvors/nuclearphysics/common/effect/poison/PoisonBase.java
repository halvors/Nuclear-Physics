package org.halvors.nuclearphysics.common.effect.poison;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;
import org.halvors.nuclearphysics.api.effect.poison.IPoison;
import org.halvors.nuclearphysics.api.item.armor.IAntiPoisonArmor;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet.EntityEquipmentSlot;
import org.halvors.nuclearphysics.common.effect.potion.PotionBase;

import java.awt.*;
import java.util.EnumSet;

public abstract class PoisonBase extends PotionBase implements IPoison {
    protected final EnumPoisonType type;
    protected final DamageSource damageSource;

    public PoisonBase(final boolean isBadEffect, final int color, final EnumPoisonType type) {
        super(isBadEffect, color, type.getName());

        this.type = type;
        this.damageSource = new DamageSource(type.getName()).setDamageBypassesArmor();
    }

    public PoisonBase(final boolean isBadEffect, final int red, final int green, final int blue, final EnumPoisonType type) {
        this(isBadEffect, new Color(red, green, blue).getRGB(), type);
    }

    public DamageSource getDamageSource() {
        return damageSource;
    }

    @Override
    public boolean isEntityProtected(final EntityLivingBase entity, final int amplifier) {
        final EnumSet<EntityEquipmentSlot> armorWorn = EnumSet.noneOf(EntityEquipmentSlot.class);

        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;

            for (final ItemStack itemStack : player.inventory.armorInventory) {
                if (itemStack != null) {
                    final Item item = itemStack.getItem();

                    if (item instanceof IArmorSet) {
                        final IArmorSet armorSet = (IArmorSet) item;

                        // Check that this armor part is part of the set same set.
                        if (armorSet.isArmorPartOfSet(itemStack)) {
                            // If this is a IAntiPoisonArmor, handle that.
                            if (item instanceof IAntiPoisonArmor) {
                                final IAntiPoisonArmor armor = (IAntiPoisonArmor) item;

                                if (armor.isProtectedFromPoison(itemStack, entity, type)) {
                                    armorWorn.add(armor.getArmorType());
                                    armor.onProtectFromPoison(itemStack, entity, type);
                                }
                            }

                            if (armorWorn.containsAll(armorSet.getArmorPartsRequired())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void poisonEntity(final EntityLivingBase entity, final int amplifier) {
        if (!isEntityProtected(entity, amplifier)) {
            performPoisonEffect(entity, amplifier);
        }
    }

    public void poisonEntity(final EntityLivingBase entity) {
        poisonEntity(entity, 0);
    }

    protected abstract void performPoisonEffect(final EntityLivingBase entity, final int amplifier);
}
