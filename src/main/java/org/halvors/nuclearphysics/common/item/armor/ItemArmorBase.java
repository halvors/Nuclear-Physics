package org.halvors.nuclearphysics.common.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

import javax.annotation.Nonnull;

public class ItemArmorBase extends ItemArmor implements IArmorSet, ISpecialArmor {
    protected final String name;

    public ItemArmorBase(final String name, final ArmorMaterial material, final EntityEquipmentSlot slot) {
        super(material, 0, slot.ordinal());

        this.name = name;

        setUnlocalizedName(Reference.ID + "." + name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }

    @Override
    public EntityEquipmentSlot getArmorType() {
        return EntityEquipmentSlot.values()[armorType];
    }

    @Override
    public boolean isArmorPartOfSet(final ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (item instanceof IArmorSet) {
            final IArmorSet armorSet = (IArmorSet) item;

            return armorSet.getArmorType() == getArmorType();
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ArmorProperties getProperties(final EntityLivingBase entity, final ItemStack itemStack, final DamageSource damageSource, final double damage, final int slot) {
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(final EntityPlayer player, final ItemStack itemStack, final int slot) {
        return 0;
    }

    @Override
    public void damageArmor(final EntityLivingBase entity, final ItemStack itemStack, final DamageSource source, final int damage, final int slot) {

    }
}
