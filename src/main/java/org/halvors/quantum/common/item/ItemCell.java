package org.halvors.quantum.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.util.LanguageUtils;

import java.util.List;

public class ItemCell extends ItemTextured {
    public ItemCell(String name) {
        super(name);

        if (!name.equalsIgnoreCase("cellEmpty")) {
            setContainerItem(Quantum.itemCell);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String localized = LanguageUtils.localize(getUnlocalizedName() + "." + itemStack.getMetadata() + ".name");

        if (localized != null && !localized.isEmpty()) {
            return getUnlocalizedName() + "." + itemStack.getMetadata();
        }

        return getUnlocalizedName();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {

    }
}