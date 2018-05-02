package org.halvors.nuclearphysics.client.render.particle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRadioactive extends ParticleSmokeNormal {
    public ParticleRadioactive(final World world, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1);

        setRBGColorF(0.2F, 0.8F, 0);
    }
}
