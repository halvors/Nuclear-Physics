package org.halvors.quantum.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.type.Color;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemBlockTooltip extends ItemBlockQuantum {
    public ItemBlockTooltip(Block block) {
        super(block);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean flag) {
        String tooltip = LanguageUtility.localize(getUnlocalizedName(itemStack) + ".tooltip");

        if (tooltip.length() > 0) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                list.add(LanguageUtility.localize("tooltip.noShift").replace("%0", Color.AQUA.toString()).replace("%1", Color.GREY.toString()));
            } else {
                list.addAll(LanguageUtility.splitStringPerWord(tooltip, 5));
            }
        }
    }
}
