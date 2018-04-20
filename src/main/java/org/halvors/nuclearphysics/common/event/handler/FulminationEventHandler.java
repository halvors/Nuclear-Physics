package org.halvors.nuclearphysics.common.event.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import org.halvors.nuclearphysics.api.effect.explosion.IFulmination;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.tile.particle.TileFulminationGenerator;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class FulminationEventHandler {
    private static final List<TileFulminationGenerator> list = new ArrayList<>();

    public static void register(TileFulminationGenerator tile) {
        if (!list.contains(tile)) {
            list.add(tile);
        }
    }

    public static void unregister(TileFulminationGenerator tile) {
        list.remove(tile);
    }

    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        final World world = event.world;
        final Explosion explosion = event.explosion;
        int x = (int) explosion.explosionX;
        int y = (int) explosion.explosionY;
        int z = (int) explosion.explosionZ;

        if (explosion instanceof IFulmination) {
            final IFulmination customExplosion = (IFulmination) explosion;

            if (customExplosion.getRadius() > 0 && customExplosion.getEnergy() > 0) {
                final HashSet<TileFulminationGenerator> avaliableGenerators = new HashSet<>();

                for (TileFulminationGenerator tile : list) {
                    if (tile != null) {
                        if (!tile.isInvalid()) {
                            final Position tilePos = new Position(tile).translate(0.5);
                            double distance = tilePos.distance(x, y, z);

                            if (distance <= customExplosion.getRadius() && distance > 0) {
                                float density = world.getBlockDensity(Vec3.createVectorHelper(x, y, z), Objects.requireNonNull(ModBlocks.blockFulmination.getCollisionBoundingBoxFromPool(world, tile.xCoord, tile.yCoord, tile.zCoord)));

                                if (density < 1) {
                                    avaliableGenerators.add(tile);
                                }
                            }
                        }
                    }
                }

                final float totalEnergy = customExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulminationGenerator tile : avaliableGenerators) {
                    //float density = event.world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tile.getPos()));
                    float density = world.getBlockDensity(Vec3.createVectorHelper(x, y, z), Objects.requireNonNull(ModBlocks.blockFulmination.getCollisionBoundingBoxFromPool(world, tile.xCoord, tile.yCoord, tile.zCoord)));
                    double distance = new Position(tile).distance(x, y, z);
                    int energy = (int) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (distance / customExplosion.getRadius()));
                    energy = (int) Math.max((1 - density) * energy, 0);

                    tile.getEnergyStorage().receiveEnergy(energy, false);
                }
            }
        }
    }
}
