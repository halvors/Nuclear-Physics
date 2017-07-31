package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.utility.LanguageUtility;

import java.util.List;

public class ItemUranium extends ItemRadioactive {
    public ItemUranium() {
        super("uranium");

        setMaxDamage(0);
    }

    @Override
    public void registerItemModel() {
        for (EnumUranium type : EnumUranium.values()) {
            Quantum.getProxy().registerItemRenderer(this, type.ordinal(), name);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        list.add(LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for (EnumUranium type : EnumUranium.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    public enum EnumUranium {
        URANIUM_235("uranium_235"),
        URANIUM_238("uranium_238");

        private String name;

        EnumUranium(String name) {
            this.name = name;
        }

        public String getName() {
            return name.toLowerCase();
        }
    }
}