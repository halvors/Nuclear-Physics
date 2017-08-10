package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;

public class AntimatterExplosion extends Explosion implements IExplosion {
    private int tier;

    public AntimatterExplosion(World world, Entity entity, double x, double y, double z, float size, int tier) {
        super(world, entity, x, y, z, size + 2 * tier, false, false);

        this.tier = tier;
    }

    @Override
    public float getRadius() {
        return getAffectedBlockPositions().size();
    }

    @Override
    public long getEnergy() {
        return (long) ((2000000000000000L + (2000000000000000L * 9 * tier)) * ConfigurationManager.General.fulminationOutputMultiplier);
    }

    @Override
    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }
}