package com.calclavia.core.lib.prefab.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class CustomDamageSource
        extends DamageSource
{
    public static ElectricalDamage electrocution = new ElectricalDamage(null);
    protected Object damageSource;

    public CustomDamageSource(String damageType)
    {
        super(damageType);
    }

    public CustomDamageSource(String damageType, Object source)
    {
        this(damageType);
        this.damageSource = source;
    }

    public CustomDamageSource setDamageBypassesArmor()
    {
        super.setDamageBypassesArmor();
        return this;
    }

    public CustomDamageSource setDamageAllowedInCreativeMode()
    {
        super.setDamageAllowedInCreativeMode();
        return this;
    }

    public CustomDamageSource setFireDamage()
    {
        super.setFireDamage();
        return this;
    }

    public CustomDamageSource setProjectile()
    {
        super.setProjectile();
        return this;
    }

    public Entity func_76346_g()
    {
        return (this.damageSource instanceof Entity) ? (Entity)this.damageSource : null;
    }

    public TileEntity getTileEntity()
    {
        return (this.damageSource instanceof TileEntity) ? (TileEntity)this.damageSource : null;
    }

    public IChatComponent func_151519_b(EntityLivingBase victum)
    {
        EntityLivingBase attacker = victum.func_94060_bK();
        String deathTranslation = "death.attack." + this.damageType;
        String playerKillTranslation = deathTranslation + ".player";
        String machineKillTranslation = deathTranslation + ".machine";
        if ((this.damageSource instanceof TileEntity))
        {
            if (StatCollector.canTranslate(machineKillTranslation)) {
                return new ChatComponentTranslation(machineKillTranslation, new Object[] { victum.getFormattedCommandSenderName() });
            }
        }
        else if (attacker != null)
        {
            if (StatCollector.canTranslate(playerKillTranslation)) {
                return new ChatComponentTranslation(playerKillTranslation, new Object[] { victum.getFormattedCommandSenderName(), attacker.getFormattedCommandSenderName() });
            }
        }
        else if (StatCollector.canTranslate(deathTranslation)) {
            return new ChatComponentTranslation(deathTranslation, new Object[] { victum.getFormattedCommandSenderName() });
        }
        return null;
    }
}
