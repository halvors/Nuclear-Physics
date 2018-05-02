package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;

public class RadioactiveExplosion extends ExplosionBase {
    public RadioactiveExplosion(final IBlockAccess world, final Entity entity, final int x, final int y, final int z, final float size, final boolean flaming, final boolean damagesTerrain) {
        super(world, entity, x, y, z, size, flaming, damagesTerrain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doExplosionA() {
        final float radius = size * 4;
        final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
        final List<EntityLiving> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (EntityLiving entity : entitiesNearby) {
            ModPotions.poisonRadiation.poisonEntity(entity);
        }

        super.doExplosionA();
    }
}
