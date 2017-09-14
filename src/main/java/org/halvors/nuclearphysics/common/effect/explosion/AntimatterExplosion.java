package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;

public class AntimatterExplosion extends RadioactiveExplosion implements IExplosion {
    private final int tier;

    public AntimatterExplosion(World world, Entity entity, BlockPos pos, float size, int tier) {
        super(world, entity, pos, size + 2 * tier, false, true);

        this.tier = tier;
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