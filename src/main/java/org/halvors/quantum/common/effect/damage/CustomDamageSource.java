package org.halvors.quantum.common.effect.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class CustomDamageSource extends DamageSource {
    private Object damageSource;

    public CustomDamageSource(String damageType)
    {
        super(damageType);
    }

    public CustomDamageSource(String damageType, Object damageSource) {
        this(damageType);
        this.damageSource = damageSource;
    }

    public CustomDamageSource setDamageBypassesArmor() {
        super.setDamageBypassesArmor();

        return this;
    }

    public CustomDamageSource setDamageAllowedInCreativeMode() {
        super.setDamageAllowedInCreativeMode();

        return this;
    }

    public CustomDamageSource setFireDamage() {
        super.setFireDamage();

        return this;
    }

    public CustomDamageSource setProjectile() {
        super.setProjectile();

        return this;
    }

    @Override
    public Entity getEntity() {
        return damageSource instanceof Entity ? (Entity) damageSource : null;
    }

    public TileEntity getTileEntity() {
        return damageSource instanceof TileEntity ? (TileEntity) damageSource : null;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase victum) {
        EntityLivingBase attacker = victum.func_94060_bK();
        String deathTranslation = "death.attack." + this.damageType;
        String playerKillTranslation = deathTranslation + ".player";
        String machineKillTranslation = deathTranslation + ".machine";

        if (damageSource instanceof TileEntity) {
            if (StatCollector.canTranslate(machineKillTranslation)) {
                return new ChatComponentTranslation(machineKillTranslation, victum.getFormattedCommandSenderName());
            }
        } else if (attacker != null) {
            if (StatCollector.canTranslate(playerKillTranslation)) {
                return new ChatComponentTranslation(playerKillTranslation, victum.getFormattedCommandSenderName(), attacker.getFormattedCommandSenderName());
            }
        } else if (StatCollector.canTranslate(deathTranslation)) {
            return new ChatComponentTranslation(deathTranslation, victum.getFormattedCommandSenderName());
        }

        return null;
    }
}
