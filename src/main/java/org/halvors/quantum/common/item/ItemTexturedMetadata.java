package org.halvors.quantum.common.item;

import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.item.ItemTextured;
import org.halvors.quantum.common.utility.LanguageUtility;

public class ItemTexturedMetadata extends ItemTextured {
    public ItemTexturedMetadata(String name) {
        super(name);

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return LanguageUtility.localize(getUnlocalizedName() + "." + itemStack.getMetadata());
    }
}
