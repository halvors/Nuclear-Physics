package org.halvors.nuclearphysics.common.item;

import net.java.games.input.Keyboard;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTooltip extends ItemBase {
    public ItemTooltip(final String name) {
        super(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(final ItemStack itemStack, @Nullable final World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        final String tooltip = getTranslationKey(itemStack) + ".tooltip";

        if (LanguageUtility.canTranselate(tooltip)) {
            if (!Keyboard.isKeyDown(Keyboard)) {
                list.add(LanguageUtility.transelate("tooltip." + Reference.ID + ".noShift", EnumColor.AQUA.toString(), EnumColor.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tooltip), 5));
            }
        }
    }

    /*
    @Override
    @Nonnull
    public String getTranslationKey(final ItemStack itemStack) {
        itemStack

        if (itemStack.getHasSubtypes()) {
            return super.getTranslationKey(itemStack) + "." + itemStack.getItemDamage();
        }

        return super.getTranslationKey(itemStack);
    }
    */
}
