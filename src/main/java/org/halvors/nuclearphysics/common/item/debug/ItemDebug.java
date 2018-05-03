package org.halvors.nuclearphysics.common.item.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.system.ThermalSystem;

import javax.annotation.Nonnull;

public class ItemDebug extends ItemBase {
    public ItemDebug() {
        super("debug");
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(final ItemStack itemStack, final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (!world.isRemote) {
            final int temperature = ThermalSystem.getTemperature(world, pos);

            if (!player.isSneaking()) {
                player.sendMessage(new TextComponentString("Temperature is: " + temperature + "K."));
            } else {
                final int amount = 100;

                player.sendMessage(new TextComponentString("Increasing temperature by 100K, temperature is now: " + (temperature + amount) + "K."));
                ThermalSystem.setTemperature(world, pos, amount);
            }
        }

        return super.onItemUse(itemStack, player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
}
