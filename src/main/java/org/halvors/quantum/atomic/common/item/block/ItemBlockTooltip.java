package org.halvors.quantum.atomic.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.common.utility.LanguageUtility;
import org.halvors.quantum.atomic.common.utility.type.Color;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockTooltip extends ItemBlockQuantum {
    public ItemBlockTooltip(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        String tooltip = LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip");

        if (tooltip != null && tooltip.length() > 0) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LanguageUtility.localize("tooltip.noShift").replace("%0", Color.AQUA.toString()).replace("%1", Color.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
            // TODO: Fix this.
            //TooltipUtilityX.addTooltip(itemStack, list);
        } else {
            list.add(LanguageUtility.localize("info.recipes.tooltip").replace("%0", Color.AQUA.toString()).replace("%1", Color.GREY.toString()));
        }
    }
}
