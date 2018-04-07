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
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int int1, boolean type) {
        if (entity instanceof EntityLivingBase) {
            ModPotions.potionRadiation.poisonEntity((int) entity.posX, (int) entity.posY, (int) entity.posZ, (EntityLivingBase) entity, 1);
        }
    }
}
