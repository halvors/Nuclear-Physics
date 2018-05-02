package org.halvors.nuclearphysics.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemTooltip extends ItemBase {
    public ItemTooltip(final String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List<String> list, final boolean flag) {
        final String tooltip = getUnlocalizedName(itemStack) + ".tooltip";

        if (LanguageUtility.canTranselate(tooltip)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LanguageUtility.transelate("tooltip." + Reference.ID + ".noShift", EnumColor.AQUA.toString(), EnumColor.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tooltip), 5));
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(final ItemStack itemStack) {
        if (itemStack.getHasSubtypes()) {
            return super.getUnlocalizedName(itemStack) + "." + itemStack.getItemDamage();
        }

        return super.getUnlocalizedName(itemStack);
    }
}
