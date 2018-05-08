package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.effect.explosion.IFulmination;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.tile.particle.TileFulminationGenerator;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber
public class FulminationEventHandler {
    private static final List<TileFulminationGenerator> generators = new ArrayList<>();

    public static void register(final TileFulminationGenerator tile) {
        if (!generators.contains(tile)) {
            generators.add(tile);
        }
    }

    public static void unregister(final TileFulminationGenerator tile) {
        generators.remove(tile);
    }

    @SubscribeEvent
    public static void onExplosionDetonateEvent(final ExplosionEvent.Detonate event) {
        final World world = event.getWorld();
        final Explosion explosion = event.getExplosion();
        final BlockPos pos = new BlockPos(explosion.getPosition());

        if (explosion instanceof IFulmination) {
            final IFulmination customExplosion = (IFulmination) explosion;

            if (customExplosion.getRadius() > 0 && customExplosion.getEnergy() > 0) {
                final HashSet<TileFulminationGenerator> avaliableGenerators = new HashSet<>();

                for (final TileFulminationGenerator tile : generators) {
                    if (tile != null && !tile.isInvalid()) {
                        final double distance = new Position(tile).translate(0.5).distance(pos.getX(), pos.getY(), pos.getZ());

                        if (distance <= customExplosion.getRadius() && distance > 0) {
                            final double density = world.getBlockDensity(new Vec3d(pos), Objects.requireNonNull(ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos())));

                            if (density < 1) {
                                avaliableGenerators.add(tile);
                            }
                        }
                    }
                }

                final float totalEnergy = customExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulminationGenerator tile : avaliableGenerators) {
                    //float density = event.worldgen.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.worldgen, tile.getPos()));
                    final double density = world.getBlockDensity(new Vec3d(pos), Objects.requireNonNull(ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos())));
                    final double distance = new Position(tile).distance(pos.getX(), pos.getY(), pos.getZ());
                    int energy = (int) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (distance / customExplosion.getRadius()));
                    energy = (int) Math.max((1 - density) * energy, 0);

                    tile.getEnergyStorage().receiveEnergy(energy, false);
                }
            }
        }
    }
}
