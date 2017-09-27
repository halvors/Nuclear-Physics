package org.halvors.nuclearphysics.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTooltip extends ItemBase {
    public ItemTooltip(String name) {
        super(name);
    }

    @Nonnull
    public String getUnlocalizedName(ItemStack itemStack) {
        if (itemStack.getHasSubtypes()) {
            return super.getUnlocalizedName(itemStack) + "." + itemStack.getItemDamage();
        }

        return super.getUnlocalizedName(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        String tooltip = getUnlocalizedName(itemStack) + ".tooltip";

        if (LanguageUtility.canTranselate(tooltip)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LanguageUtility.transelate("tooltip." + Reference.ID + ".noShift", Color.AQUA.toString(), Color.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tooltip), 5));
            }
        }
    }
}
