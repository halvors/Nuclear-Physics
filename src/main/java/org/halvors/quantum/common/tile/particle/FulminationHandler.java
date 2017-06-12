package org.halvors.quantum.common.tile.particle;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.explotion.ExplosionEvent;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FulminationHandler {
    public static final FulminationHandler INSTANCE = new FulminationHandler();
    public static final List<TileFulmination> list = new ArrayList<>();

    public void register(TileFulmination tileEntity) {
        if (!list.contains(tileEntity)) {
            list.add(tileEntity);
        }
    }

    public void unregister(TileFulmination tileEntity) {
        list.remove(tileEntity);
    }

    @SubscribeEvent
    public void BaoZha(ExplosionEvent.DoExplosionEvent event) {
        if (event.iExplosion != null) {
            if (event.iExplosion.getRadius() > 0 && event.iExplosion.getEnergy() > 0) {
                HashSet<TileFulmination> avaliableGenerators = new HashSet<>();

                for (TileFulmination tileEntity : FulminationHandler.list) {
                    if (tileEntity != null) {
                        if (!tileEntity.isInvalid()) {
                            Vector3 tileDiDian = new Vector3(tileEntity);
                            tileDiDian.translate(0.5f);
                            double juLi = tileDiDian.distance(new Vector3(event.x, event.y, event.z));

                            if (juLi <= event.iExplosion.getRadius() && juLi > 0) {
                                float miDu = event.world.getBlockDensity(Vec3.createVectorHelper(event.x, event.y, event.z), Quantum.blockFulmination.getBlockType().getCollisionBoundingBoxFromPool(event.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));

                                if (miDu < 1) {
                                    avaliableGenerators.add(tileEntity);
                                }
                            }
                        }
                    }
                }

                final float totalEnergy = event.iExplosion.getEnergy();
                final float maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();

                for (TileFulmination tileEntity : avaliableGenerators) {
                    float density = event.world.getBlockDensity(Vec3.createVectorHelper(event.x, event.y, event.z), Quantum.blockFulmination.getBlockType().getCollisionBoundingBoxFromPool(event.world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
                    double juLi = new Vector3(tileEntity).distance(new Vector3(event.x, event.y, event.z));
                    long energy = (long) Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (juLi / event.iExplosion.getRadius()));
                    energy = (long) Math.max((1 - density) * energy, 0);

                    tileEntity.getEnergyHandler().receiveEnergy((int) energy, false);
                }
            }
        }
    }
}
