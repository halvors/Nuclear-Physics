package org.halvors.atomicscience.old.process;

import atomicscience.TabAS;
import calclavia.api.atomicscience.IAntiPoisonArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemHazmat
        extends ItemArmor
        implements IAntiPoisonArmor
{
    public ItemHazmat(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
    {
        super(par1, par2EnumArmorMaterial, par3, par4);
        func_77637_a(TabAS.INSTANCE);
        func_77656_e(200000);
    }

    public Item func_77655_b(String par1Str)
    {
        super.func_77655_b(par1Str);
        func_111206_d(par1Str);
        return this;
    }

    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        return "atomicscience:models/hazmat.png";
    }

    public boolean isProtectedFromPoison(ItemStack itemStack, EntityLivingBase entityLiving, String type)
    {
        return (type.equalsIgnoreCase("radiation")) || (type.equalsIgnoreCase("chemical")) || (type.equalsIgnoreCase("contagious"));
    }

    public void onProtectFromPoison(ItemStack itemStack, EntityLivingBase entityLiving, String type)
    {
        itemStack.func_77972_a(1, entityLiving);
    }

    public int getArmorType()
    {
        return this.field_77881_a;
    }
}
