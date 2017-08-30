package org.halvors.nuclearphysics.common.event.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.explosion.ExplosionEvent;
import org.halvors.nuclearphysics.common.tile.particle.TileFulmination;
import org.halvors.nuclearphysics.common.utility.position.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FulminationHandler {
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
        if (event.iExplosion != null) {
            if (event.iExplosion.getRadius() > 0 && event.iExplosion.getEnergy() > 0) {
                HashSet<TileFulmination> avaliableGenerators = new HashSet<>();

                for (TileFulmination tileEntity : FulminationHandler.list) {
                    if (tileEntity != null) {
                        if (!tileEntity.isInvalid()) {
                            Position tileDiDian = new Position(tileEntity);
                            tileDiDian.translate(0.5f);
                            double juLi = tileDiDian.distance(event.x, event.y, event.z);

                            if (juLi <= event.iExplosion.getRadius() && juLi > 0) {
                                float miDu = 0; //event.world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

                                if (miDu < 1) {
                                    avaliableGenerators.add(tileEntity);
                                }
                            }
                        }
                    }
                }

                final float totalEnergy = event.iExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulmination tile : avaliableGenerators) {
                    //float density = event.world.getBlockDensity(new Vec3d(event.x, event.y, event.z), QuantumBlocks.blockFulmination.getCollisionBoundingBox(event.world, tile.getPos()));
                    float density = 0;
                    double juLi = new Position(tile).distance(event.x, event.y, event.z);
                    long energy = (long) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (juLi / event.iExplosion.getRadius()));
                    energy = (long) Math.max((1 - density) * energy, 0);

                    tile.getEnergyStorage().receiveEnergy((int) energy, false);
                }
            }
        }
    }
}
