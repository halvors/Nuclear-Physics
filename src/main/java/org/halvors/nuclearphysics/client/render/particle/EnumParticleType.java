package org.halvors.nuclearphysics.client.render.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public enum EnumParticleType {
    RADIOACTIVE(ParticleRadioactive.class);

    private final Class<? extends Particle> particleClass;

    EnumParticleType(final Class<? extends Particle> particleClass) {
        this.particleClass = particleClass;
    }

    public Class<? extends Particle> getParticleClass() {
        return particleClass;
    }

    public Particle getParticleAsInstance(final World world, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed) {
        try {
            return particleClass.getDeclaredConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class).newInstance(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        } catch (Exception e) {
            NuclearPhysics.getLogger().error("Unable to indirectly create particle.");
            e.printStackTrace();
        }

        return null;
    }
}
