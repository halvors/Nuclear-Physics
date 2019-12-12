package org.halvors.nuclearphysics.common.type;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.client.render.particle.ParticleRadioactive;

public enum EnumParticleType {
    RADIOACTIVE;

    EnumParticleType() {

    }

    @OnlyIn(Dist.CLIENT)
    public Class<? extends Particle> getParticleClass() {
        switch (this) {
            case RADIOACTIVE:
                return ParticleRadioactive.class;
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public Particle getParticleAsInstance(final World world, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed) {
        Class<? extends Particle> particleClass = getParticleClass();

        if (particleClass != null) {
            try {
                return particleClass.getDeclaredConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class).newInstance(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
            } catch (Exception e) {
                NuclearPhysics.getLogger().error("Unable to indirectly create particle.");
                e.printStackTrace();
            }
        }

        return null;
    }
}
