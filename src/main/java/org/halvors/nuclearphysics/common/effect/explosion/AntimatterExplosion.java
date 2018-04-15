package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.effect.explosion.IExplosion;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;

public class AntimatterExplosion extends RadioactiveExplosion implements IExplosion {
    private final int tier;

    public AntimatterExplosion(IBlockAccess world, Entity entity, BlockPos pos, float size, int tier) {
        super(world, entity, pos, size + 2 * tier, false, true);

        this.tier = tier;
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        world.playSound(null, pos, ModSoundEvents.ANTIMATTER, SoundCategory.BLOCKS, 3, 1 - world.rand.nextFloat() * 0.3F);

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