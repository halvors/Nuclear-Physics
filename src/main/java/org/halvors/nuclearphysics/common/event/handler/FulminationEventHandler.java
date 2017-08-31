package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.explosion.ExplosionEvent;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.tile.particle.TileFulmination;
import org.halvors.nuclearphysics.common.utility.position.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FulminationEventHandler {
    private static final List<TileFulmination> list = new ArrayList<>();

    public static void register(TileFulmination tile) {
        if (!list.contains(tile)) {
            list.add(tile);
        }
    }

    public static void unregister(TileFulmination tileEntity) {
        list.remove(tileEntity);
    }

    @SubscribeEvent
    public void onExplosionEvent(ExplosionEvent.DoExplosionEvent event) {
        World world = event.world;

        if (event.iExplosion != null) {
            if (event.iExplosion.getRadius() > 0 && event.iExplosion.getEnergy() > 0) {
                HashSet<TileFulmination> avaliableGenerators = new HashSet<>();

                for (TileFulmination tile : list) {
                    if (tile != null) {
                        if (!tile.isInvalid()) {
                            Position tilePos = new Position(tile).translate(0.5);
                            double distance = tilePos.distance(event.x, event.y, event.z);

                            if (distance <= event.iExplosion.getRadius() && distance > 0) {
                                //float density = world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
                                float density = world.getBlockDensity(new Vec3d(event.x, event.y, event.z), ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos()));

                                if (density < 1) {
                                    avaliableGenerators.add(tile);
                                }
                            }
                        }
                    }
                }

                final float totalEnergy = event.iExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulmination tile : avaliableGenerators) {
                    //float density = event.world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tile.getPos()));
                    float density = world.getBlockDensity(new Vec3d(event.x, event.y, event.z), ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos()));
                    double distance = new Position(tile).distance(event.x, event.y, event.z);
                    long energy = (long) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (distance / event.iExplosion.getRadius()));
                    energy = (long) Math.max((1 - density) * energy, 0);

                    tile.getEnergyStorage().receiveEnergy((int) energy, false);
                }
            }
        }
    }
}
