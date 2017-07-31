package org.halvors.quantum.common.item;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.utility.LanguageUtility;

import javax.annotation.Nonnull;

public class ItemMetadata extends ItemQuantum {
    public ItemMetadata(String name) {
        super(name);

        setHasSubtypes(true);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
    }
}
