package org.halvors.atomicscience.old.fission;

import com.calclavia.core.lib.prefab.poison.PoisonRadiation;
import com.calclavia.core.lib.transform.vector.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRadioactive extends Item {
    public ItemRadioactive() {

    }

    public void onUpdate(ItemStack itemStack, World world, Entity entity, int par1, boolean par2) {
        if (entity instanceof EntityLivingBase) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(entity), (EntityLivingBase)entity, 1);
        }
    }
}
