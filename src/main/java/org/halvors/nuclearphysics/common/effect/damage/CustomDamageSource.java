package org.halvors.nuclearphysics.common.effect.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Override
    @Nonnull
    public CustomDamageSource setDamageBypassesArmor() {
        super.setDamageBypassesArmor();

        return this;
    }

    @Override
    @Nonnull
    public CustomDamageSource setDamageAllowedInCreativeMode() {
        super.setDamageAllowedInCreativeMode();

        return this;
    }

    @Override
    @Nonnull
    public CustomDamageSource setFireDamage() {
        super.setFireDamage();

        return this;
    }

    @Override
    @Nonnull
    public CustomDamageSource setProjectile() {
        super.setProjectile();

        return this;
    }

    @Nullable
    @Override
    public Entity getImmediateSource() {
        return getEntity();
    }

    @Nullable
    @Override
    public Entity getTrueSource() {
        return getEntity();
    }

    public Entity getEntity() {
        return damageSource instanceof Entity ? (Entity) damageSource : null;
    }

    public TileEntity getTileEntity() {
        return damageSource instanceof TileEntity ? (TileEntity) damageSource : null;
    }

    @Override
    @Nonnull
    public ITextComponent getDeathMessage(EntityLivingBase victum) {
        EntityLivingBase attacker = victum.getAttackingEntity();
        String deathTranslation = "death.attack." + this.damageType;
        String playerKillTranslation = deathTranslation + ".player";
        String machineKillTranslation = deathTranslation + ".machine";

        if (damageSource instanceof TileEntity) {
            if (LanguageUtility.canTranselate(machineKillTranslation)) {
                return new TextComponentTranslation(machineKillTranslation, victum.getCommandSenderEntity().getDisplayName());
            }
        } else if (attacker != null) {
            if (LanguageUtility.canTranselate(playerKillTranslation)) {
                return new TextComponentTranslation(playerKillTranslation, victum.getCommandSenderEntity().getDisplayName(), attacker.getCommandSenderEntity().getDisplayName());
            }
        } else if (LanguageUtility.canTranselate(deathTranslation)) {
            return new TextComponentTranslation(deathTranslation, victum.getCommandSenderEntity().getDisplayName());
        }

        return null;
    }
}
