package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.init.ModSounds;

public class AntimatterExplosion extends RadioactiveExplosion implements IExplosion {
    private final int tier;

    public AntimatterExplosion(IBlockAccess world, Entity entity, int x, int y, int z, float size, int tier) {
        super(world, entity, x, y, z, size + 2 * tier, false, true);

        this.tier = tier;
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        world.playSoundEffect(x, y, z, ModSounds.ANTIMATTER, 3, 1 - world.rand.nextFloat() * 0.3F);

        super.doExplosionB(spawnParticles);
    }

    @Override
    public float getRadius() {
        return size;
    }

    @Override
    public int getEnergy() {
        int multiplier = tier + 1;

        return (int) ((3 * ((29 * 100000 * multiplier^2) + (14 * 10000000 * multiplier) - 124 * 1000000) / 100) * ConfigurationManager.General.fulminationOutputMultiplier);
    }
}