package org.halvors.quantum.atomic.client.render.particle;

import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRadioactive extends ParticleSmokeNormal {
    public ParticleRadioactive(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed) {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1);

        setRBGColorF(0.2F, 0.8F, 0);
    }
}
