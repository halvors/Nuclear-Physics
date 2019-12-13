package org.halvors.nuclearphysics.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.init.ModPotions;

public class ItemRadioactive extends ItemTooltip {
    public ItemRadioactive(final String name) {
        super(name);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof LivingEntity) {
            ModPotions.poisonRadiation.poisonEntity((LivingEntity) entity, 1);
        }
    }
}
