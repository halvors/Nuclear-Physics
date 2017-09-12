package org.halvors.nuclearphysics.common.item;

import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import org.halvors.nuclearphysics.common.Integration;

@InterfaceList({
        @Interface(iface = "mekanism.api.IMekWrench", modid = Integration.MEKANISM_MOD_ID)
})
public class ItemWrench extends ItemBase implements IMekWrench {
    public ItemWrench(String name) {
        super(name);

        setMaxStackSize(1);
    }

    // Mekanism
    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer player, BlockPos pos) {
        return true;
    }
}
