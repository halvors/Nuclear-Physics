package org.halvors.nuclearphysics.common.science;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.storage.nbt.data.ChunkDataStorage;
import org.halvors.nuclearphysics.common.type.Pair;

public class ThermalEnergyStorage extends ChunkDataStorage {
    private static final ThermalEnergyStorage instance = new ThermalEnergyStorage();

    public ThermalEnergyStorage() {
        super("ThermalEnergy");
    }

    public static ThermalEnergyStorage getInstance() {
        return instance;
    }

    public double getTemperature(final IBlockAccess world, final BlockPos pos) {
        int value = getValue(world, pos);

        if (value > 0) {
            return value;
        }

        return ThermalPhysics.getTemperatureForCoordinate((World) world, pos);
    }

    public static void addTemperature(final World world, final BlockPos pos, final double deltaTemperature) {
        final Pair<World, BlockPos> key = new Pair<>(world, pos);
        final double defaultTemperature = ThermalPhysics.getTemperatureForCoordinate(world, pos);
        final double original = thermalSourceMap.getOrDefault(key, defaultTemperature);
        final double newTemperature = original + deltaTemperature;

        if (Math.abs(newTemperature - defaultTemperature) > 0.4) {
            thermalSourceMap.put(key, newTemperature);
        } else {
            thermalSourceMap.remove(key);
        }
    }
}
