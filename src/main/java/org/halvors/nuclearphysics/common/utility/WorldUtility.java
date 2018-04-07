package org.halvors.nuclearphysics.common.utility;

import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketParticle;
import org.halvors.nuclearphysics.common.type.Range;

public class WorldUtility {
    public static void spawnParticle(World world, String particleName, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        NuclearPhysics.getPacketHandler().sendToReceivers(new PacketParticle(particleName, x, y, z, velocityX, velocityY, velocityZ), new Range(world, (int) x, (int) y, (int) z));
    }
}