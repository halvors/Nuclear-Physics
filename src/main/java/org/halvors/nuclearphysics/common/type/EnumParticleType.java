package org.halvors.nuclearphysics.common.type;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.particle.ParticleRadioactive;
import org.halvors.nuclearphysics.common.NuclearPhysics;

public enum EnumParticleType {
    RADIOACTIVE;

    EnumParticleType() {

    }

    @SideOnly(Side.CLIENT)
    public Class<? extends Particle> getParticleClass() {
        if (this == EnumParticleType.RADIOACTIVE) {
            return ParticleRadioactive.class;
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public Particle getParticleAsInstance(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
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
