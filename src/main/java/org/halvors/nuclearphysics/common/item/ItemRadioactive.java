package org.halvors.nuclearphysics.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.init.ModPotions;

public class ItemRadioactive extends ItemTooltip {
    public ItemRadioactive(final String name) {
        super(name);
    }

    @Override
    public void onUpdate(final ItemStack itemStack, final World world, final Entity entity, final int int1, final boolean type) {
        if (entity instanceof EntityLivingBase) {
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity, 1);
        }
    }
}
