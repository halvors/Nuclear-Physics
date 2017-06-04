package org.halvors.quantum.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.halvors.quantum.common.poison.PoisonRadiation;
import org.halvors.quantum.common.transform.vector.Vector3;

public class ItemRadioactive extends ItemTexturedMetadata {
    public ItemRadioactive(String name) {
        super(name);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int int1, boolean type) {
        if (entity instanceof EntityLivingBase) {
            PoisonRadiation.poison.poisonEntity(new Vector3(entity), (EntityLivingBase) entity, 1);
        }
    }
}
