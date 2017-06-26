package org.halvors.quantum.common.thermal;


import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.event.ThermalEvent;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import universalelectricity.api.net.IUpdate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ThermalGrid implements IUpdate {
    private final float spread = 1 / 7F;
    private final float loss = 0.1F;
    private static final HashMap<VectorWorld, Float> thermalSource = new HashMap<>();
    private int tick = 0;
    private final float deltaTime = 1 / 20F;

    public static float getDefaultTemperature(VectorWorld position) {
        return ThermalPhysics.getTemperatureForCoordinate(position.world, position.intX(), position.intZ());
    }

    public static void addTemperature(VectorWorld position, float deltaTemperature) {
        float original;
        float defaultTemperature = getDefaultTemperature(position);


        if (thermalSource.containsKey(position)) {
            original = thermalSource.get(position);
        } else {
            original = defaultTemperature;
        }

        float newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSource.put(position, original + deltaTemperature);
        } else {
            thermalSource.remove(position);
        }
    }

    public static float getTemperature(VectorWorld position) {
        if (thermalSource.containsKey(position)) {
            return thermalSource.get(position);
        }

        return ThermalPhysics.getTemperatureForCoordinate(position.world, position.intX(), position.intZ());
    }

    @Override
    public void update() {
        Iterator<Map.Entry<VectorWorld, Float>> it = new HashMap<>(thermalSource).entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<VectorWorld, Float> entry = it.next();

            // Distribute temperature
            VectorWorld pos = entry.getKey();

            /** Deal with different block types */
            float currentTemperature = getTemperature(pos);

            if (currentTemperature < 0) {
                thermalSource.remove(pos);

                continue;
            } else {
                float deltaFromEquilibrium = getDefaultTemperature(pos) - currentTemperature;

                TileEntity possibleReactor = pos.getTileEntity();
                boolean isReactor = false;

                if (possibleReactor != null && possibleReactor instanceof IReactor) {
                    isReactor = true;
                } else {
                    isReactor = false;
                }

                ThermalEvent.ThermalUpdateEvent event = new ThermalEvent.ThermalUpdateEvent(pos, currentTemperature, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                float loss = event.heatLoss;
                addTemperature(pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(loss)));

                // Spread heat to surrounding.
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    VectorWorld adjacent = (VectorWorld) pos.clone().translate(dir);
                    float deltaTemperature = getTemperature(pos) - getTemperature(adjacent);

                    Material adjacentMat = adjacent.world.getBlock(adjacent.intX(), adjacent.intY(), adjacent.intZ()).getMaterial();

                    float spread = (adjacentMat.isSolid() ? this.spread : this.spread / 2) * deltaTime;

                    if (deltaTemperature > 0) {
                        addTemperature(adjacent, deltaTemperature * spread);
                        addTemperature(pos, -deltaTemperature * spread);
                    }
                }
            }
        }
    }

    public boolean canUpdate() {
        return true;
    }

    public boolean continueUpdate() {
        return true;
    }
}
