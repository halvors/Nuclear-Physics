package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;

public class RadioactiveExplosion extends ExplosionBase {
    public RadioactiveExplosion(final IBlockAccess world, final Entity entity, final BlockPos pos, final float size, final boolean flaming, final boolean damagesTerrain) {
        super(world, entity, pos, size, flaming, damagesTerrain);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doExplosionA() {
        final float radius = size * 4;
        final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        final List<EntityLiving> entitiesNearby = world.getEntitiesWithinAABB(EntityLiving.class, bounds);

        for (final EntityLiving entity : entitiesNearby) {
            ModPotions.poisonRadiation.poisonEntity(entity);
        }

        super.doExplosionA();
    }
}
