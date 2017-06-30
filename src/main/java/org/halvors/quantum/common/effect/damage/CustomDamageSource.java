package org.halvors.quantum.common.effect.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

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
    public ITextComponent getDeathMessage(EntityLivingBase victum) {
        EntityLivingBase attacker = victum.getAttackingEntity();
        String deathTranslation = "death.attack." + this.damageType;
        String playerKillTranslation = deathTranslation + ".player";
        String machineKillTranslation = deathTranslation + ".machine";

        if (damageSource instanceof TileEntity) {
            if (I18n.canTranslate(machineKillTranslation)) {
                return new TextComponentTranslation(machineKillTranslation, victum.getCommandSenderEntity().getDisplayName());
            }
        } else if (attacker != null) {
            if (I18n.canTranslate(playerKillTranslation)) {
                return new TextComponentTranslation(playerKillTranslation, victum.getCommandSenderEntity().getDisplayName(), attacker.getCommandSenderEntity().getDisplayName());
            }
        } else if (I18n.canTranslate(deathTranslation)) {
            return new TextComponentTranslation(deathTranslation, victum.getCommandSenderEntity().getDisplayName());
        }

        return null;
    }
}
