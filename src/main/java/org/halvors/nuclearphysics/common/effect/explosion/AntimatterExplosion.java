package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.effect.explosion.IFulmination;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModSounds;

public class AntimatterExplosion extends RadioactiveExplosion implements IFulmination {
    private final int tier;

    public AntimatterExplosion(final IBlockAccess world, final Entity entity, final BlockPos pos, final float size, final int tier) {
        super(world, entity, pos, size + 2 * tier, false, true);

        this.tier = tier;
    }

    @Override
    public void doExplosionB(final boolean spawnParticles) {
        world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), ModSounds.ANTIMATTER, 3, 1 - world.rand.nextFloat() * 0.3F);

        super.doExplosionB(spawnParticles);
    }

    @Override
    public float getRadius() {
        return size;
    }

    @Override
    public int getEnergy() {
        final int multiplier = tier + 1;

        return (int) ((3 * ((29 * 100000 * multiplier^2) + (14 * 10000000 * multiplier) - 124 * 1000000) / 100) * General.fulminationOutputMultiplier);
    }
}