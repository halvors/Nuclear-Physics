package org.halvors.quantum.lib.prefab.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.client.utility.render.Color;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockTooltip extends ItemBlock {
    public ItemBlockTooltip(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List list, boolean flag) {
        String tooltip = LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip");

        if ((tooltip != null) && (tooltip.length() > 0)) {
            if (!Keyboard.isKeyDown(42)) {
                list.add(LanguageUtility.localize("tooltip.noShift").replace("%0", Color.AQUA.toString()).replace("%1", Color.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }

        if (Keyboard.isKeyDown(36)) {
            // TODO: Fix this.
            //TooltipUtility.addTooltip(itemStack, list);
        } else {
            list.add(LanguageUtility.localize("info.recipes.tooltip").replace("%0", Color.AQUA.toString()).replace("%1", Color.GREY.toString()));
        }
    }
}
