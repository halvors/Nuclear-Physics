package org.halvors.quantum.atomic.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.quantum.atomic.common.effect.poison.PoisonRadiation;

public class ItemRadioactive extends ItemTooltip {
    public ItemRadioactive(String name) {
        super(name);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int int1, boolean type) {
        if (entity instanceof EntityLivingBase) {
            PoisonRadiation.getInstance().poisonEntity(entity.getPosition(), (EntityLivingBase) entity, 1);
        }
    }
}
