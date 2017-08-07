package org.halvors.quantum.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.type.Color;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemTooltip extends ItemQuantum {
    public ItemTooltip(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean flag) {
        String tooltip = getUnlocalizedName(itemStack) + ".tooltip";

        if (LanguageUtility.canTranselate(tooltip)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LanguageUtility.transelate("tooltip.noShift", Color.AQUA.toString(), Color.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tooltip), 5));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getHasSubtypes()) {
            return getUnlocalizedName() + "." + itemStack.getItemDamage();
        }

        return getUnlocalizedName();
    }
}
