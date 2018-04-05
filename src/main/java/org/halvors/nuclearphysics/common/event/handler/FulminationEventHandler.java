package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.explosion.IExplosion;
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
        final World world = event.getWorld();
        final Explosion explosion = event.getExplosion();
        final BlockPos pos = new BlockPos(explosion.getPosition());

        if (explosion instanceof IExplosion) {
            final IExplosion customExplosion = (IExplosion) explosion;

            if (customExplosion.getRadius() > 0 && customExplosion.getEnergy() > 0) {
                final HashSet<TileFulminationGenerator> avaliableGenerators = new HashSet<>();

                for (TileFulminationGenerator tile : list) {
                    if (tile != null) {
                        if (!tile.isInvalid()) {
                            double distance = new Position(tile).translate(0.5).distance(pos.getX(), pos.getY(), pos.getZ());

                            if (distance <= customExplosion.getRadius() && distance > 0) {
                                //float density = world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
                                float density = world.getBlockDensity(new Vec3d(pos), Objects.requireNonNull(ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos())));

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
                    float density = world.getBlockDensity(new Vec3d(pos), Objects.requireNonNull(ModBlocks.blockFulmination.getDefaultState().getCollisionBoundingBox(world, tile.getPos())));
                    double distance = new Position(tile).distance(pos.getX(), pos.getY(), pos.getZ());
                    int energy = (int) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (distance / customExplosion.getRadius()));
                    energy = (int) Math.max((1 - density) * energy, 0);

                    tile.getEnergyStorage().receiveEnergy(energy, false);
                }
            }
        }
    }
}
