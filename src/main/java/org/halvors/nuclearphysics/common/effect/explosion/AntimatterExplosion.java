package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.Reference;

public class AntimatterExplosion extends RadioactiveExplosion implements IExplosion {
    private final int tier;

    public AntimatterExplosion(IBlockAccess world, Entity entity, int x, int y, int z, float size, int tier) {
        super(world, entity, x, y, z, size + 2 * tier, false, true);

        this.tier = tier;
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        world.playSoundEffect(x, y, z, Reference.PREFIX + "antimatter", 3, 1 - world.rand.nextFloat() * 0.3F);

        super.doExplosionB(spawnParticles);
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