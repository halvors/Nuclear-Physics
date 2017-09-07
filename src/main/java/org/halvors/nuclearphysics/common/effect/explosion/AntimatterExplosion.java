package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;

public class AntimatterExplosion extends Explosion implements IExplosion {
    private float size;
    private int tier;

    public AntimatterExplosion(World world, Entity entity, BlockPos pos, float size, int tier) {
        super(world, entity, pos.getX(), pos.getY(), pos.getZ(), size + 2 * tier, false, true);

        this.size = size + 2 * tier;
        this.tier = tier;
    }

    @Override
    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }

    @Override
    public float getRadius() {
        return size;
    }

    @Override
    public long getEnergy() {
        return (long) ((2000000000000000L + (2000000000000000L * 9 * tier)) * ConfigurationManager.General.fulminationOutputMultiplier);
    }
}