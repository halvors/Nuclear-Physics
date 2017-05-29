package org.halvors.atomicscience.old.base;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.atomicscience.old.AtomicScience;
import org.halvors.atomicscience.old.util.LanguageUtility;

public class ItemCell extends Item
{
    public ItemCell()
    {
        setContainerItem(AtomicScience.itemCell);
    }

    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
    {
        String tooltip = LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip");
        if ((tooltip != null) && (tooltip.length() > 0)) {

            /* TODO: Fix this.
            if (!Keyboard.isKeyDown(42)) {
                list.add(LanguageUtility.localize("tooltip.noShift").replace("%0", EnumColor.AQUA.toString()).replace("%1", EnumColor.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
            */
        }
    }

    public String func_77667_c(ItemStack itemstack)
    {
        String localized = LanguageUtility.getLocal(getUnlocalizedName() + "." + itemstack.getMetadata() + ".name");
        if ((localized != null) && (!localized.isEmpty())) {
            return getUnlocalizedName() + "." + itemstack.getMetadata();
        }
        return getUnlocalizedName();
    }
}