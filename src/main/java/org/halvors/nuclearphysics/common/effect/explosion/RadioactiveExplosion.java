package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;

public class RadioactiveExplosion extends ExplosionBase {
    public RadioactiveExplosion(IBlockAccess world, Entity entity, int x, int y, int z, float size, boolean flaming, boolean damagesTerrain) {
        super(world, entity, x, y, z, size, flaming, damagesTerrain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doExplosionA() {
        float radius = size * 4;
        AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        List<EntityLiving> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (EntityLiving entity : entitiesNearby) {
            ModPotions.potionRadiation.poisonEntity(entity.serverPosX, entity.serverPosY, entity.serverPosZ, entity);
        }

        super.doExplosionA();
    }
}
