package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.item.ItemRadioactive;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemUranium extends ItemRadioactive {
    public ItemUranium() {
        super("uranium");

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public void registerItemModel() {
        for (EnumUranium type : EnumUranium.values()) {
            Quantum.getProxy().registerItemRenderer(this, type.ordinal(), name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (EnumUranium type : EnumUranium.values()) {
            items.add(new ItemStack(this, 1, type.ordinal()));
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