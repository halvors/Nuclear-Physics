package org.halvors.quantum.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.quantum.api.explotion.IExplosion;
import org.halvors.quantum.common.ConfigurationManager;

public class AntimatterExplosion extends Explosion implements IExplosion {
    private int tier;

    public AntimatterExplosion(World world, Entity entity, double x, double y, double z, float size, int tier) {
        super(world, entity, x, y, z, size + 2 * tier);
        this.tier = tier;
    }

    @Override
    public float getRadius() {
        return this.explosionSize;
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