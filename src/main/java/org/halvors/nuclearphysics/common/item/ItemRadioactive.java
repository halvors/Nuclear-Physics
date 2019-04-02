package org.halvors.nuclearphysics.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.init.ModPotions;

public class ItemRadioactive extends ItemTooltip {
    public ItemRadioactive(String name) {
        super(name);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityLivingBase) {
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity, 1);
        }
    }
}
