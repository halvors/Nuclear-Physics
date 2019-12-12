package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;

public class RadioactiveExplosion extends ExplosionBase {
    public RadioactiveExplosion(final IWorld world, final Entity entity, final BlockPos pos, final float size, final boolean flaming, final boolean damagesTerrain) {
        super(world, entity, pos, size, flaming, damagesTerrain);
    }

    @Override
    public void doExplosionA() {
        final float radius = size * 4;
        final AxisAlignedBB bounds = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        final List<LivingEntity> entitiesNearby = world.getEntitiesWithinAABB(LivingEntity.class, bounds);

        for (final LivingEntity entity : entitiesNearby) {
            ModPotions.poisonRadiation.poisonEntity(entity);
        }

        super.doExplosionA();
    }
}
