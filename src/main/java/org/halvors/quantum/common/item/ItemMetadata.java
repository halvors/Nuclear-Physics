package org.halvors.quantum.common.item;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.utility.LanguageUtility;

public class ItemMetadata extends ItemQuantum {
    public ItemMetadata(String name) {
        super(name);

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
    }
}
