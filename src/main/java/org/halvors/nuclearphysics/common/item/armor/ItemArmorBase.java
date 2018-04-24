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

public class ItemArmorBase extends ItemArmor implements IArmorSet, ISpecialArmor {
    protected final String name;

    public ItemArmorBase(String name, ArmorMaterial material, EntityEquipmentSlot slot) {
        super(material, 0, slot.ordinal());

        this.name = name;

        setUnlocalizedName(Reference.ID + "." + name);
        setTextureName(Reference.PREFIX + name);
        setCreativeTab(NuclearPhysics.getCreativeTab());
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot() {
        return EntityEquipmentSlot.values()[armorType];
    }

    @Override
    public boolean isArmorPartOfSet(ItemStack itemStack) {
        final Item item = itemStack.getItem();

        if (item instanceof IArmorSet) {
            IArmorSet armorSet = (IArmorSet) item;

            return armorSet.getEquipmentSlot() == getEquipmentSlot();
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ArmorProperties getProperties(EntityLivingBase entity, ItemStack itemStack, DamageSource damageSource, double damage, int slot) {
        return new ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack itemStack, DamageSource source, int damage, int slot) {

    }
}
