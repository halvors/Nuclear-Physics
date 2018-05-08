package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A thermal block manager
 *
 * @author Calclavia
 */
public class ThermalPhysics {
    public static final double ROOM_TEMPERATURE = 295;
    public static final double ICE_MELT_TEMPERATURE = 273.15;
    public static final double WATER_BOIL_TEMPERATURE = 373.2;

    private static final Map<Material, Integer> materialSHCMap = new HashMap<>();

    static {
        /*
         * TODO: Data should be fetched from: https://www.engineeringtoolbox.com/specific-heat-capacity-d_391.html
         *                                    https://www.engineeringtoolbox.com/specific-heat-solids-d_154.html
         */

        register(Material.IRON, 449); // Updated.
        register(Material.AIR, 1005); // Updated.
        register(Material.GROUND, 8000);
        register(Material.WOOD, 2000); // Updated.
        register(Material.ROCK, 8400);
        register(Material.ANVIL, 5000);
        register(Material.WATER, 4182); // Updated.
        register(Material.LAVA, 9000);
        register(Material.LEAVES, 8400);
        register(Material.PLANTS, 8400);
        register(Material.VINE, 8400);
        register(Material.SPONGE, 8400);
        register(Material.CLOTH, 1400);
        register(Material.FIRE, 1000);
        register(Material.SAND, 800); // Updated.
        register(Material.CIRCUITS, 1000);
        register(Material.CARPET, 1130);
        register(Material.GLASS, 840); // Updated.
        register(Material.REDSTONE_LIGHT, 1000);
        register(Material.TNT, 1000);
        register(Material.CORAL, 8400);
        register(Material.ICE, 2090); // Updated.
        register(Material.SNOW, 2090);
        register(Material.CRAFTED_SNOW, 4200);
        register(Material.CACTUS, 8400);
        register(Material.CLAY, 920); // Updated.
        register(Material.GOURD, 8400);
        register(Material.DRAGON_EGG, 8400);
        register(Material.PORTAL, 1000);
        register(Material.CAKE, 2000);
        register(Material.WEB, 8400);
        register(Material.PISTON, 4500);
    }

    /**
     * Registers a block withPriority a specific heating value
     * @param specificHeatCapacity - The specific heat capacity in J/Kg K
     */
    public static void register(final Material material, int specificHeatCapacity) {
        if (material != null && specificHeatCapacity > 0) {
            materialSHCMap.put(material, specificHeatCapacity);
        }
    }

    /**
     * Gets the specific heat capacity of a certain material
     */
    public static int getSpecificHeatCapacity(final Material material) {
        return materialSHCMap.getOrDefault(material, 0);
    }

    public static double getTemperatureForEnergy(double mass, double specificHeatCapacity, double energy) {
        return energy / (mass * specificHeatCapacity);
    }

    public static double getRequiredBoilWaterEnergy(World world, int x, int z) {
        return getRequiredBoilWaterEnergy(world, x, z, Fluid.BUCKET_VOLUME);
    }

    public static double getRequiredBoilWaterEnergy(World world, int x, int z, int volume) {
        double temperatureChange = WATER_BOIL_TEMPERATURE - getDefaultTemperature(world, new BlockPos(x, 0, z));
        double mass = getMass(volume, 1);

        return getEnergyForTemperatureChange(mass, getSpecificHeatCapacity(Material.WATER), temperatureChange) + ThermalPhysics.getEnergyForStateChange(mass, 2257000);
    }

    /**
     * Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static double getDefaultTemperature(final World world, final BlockPos pos) {
        final double averageTemperature = ICE_MELT_TEMPERATURE + (world.getBiome(pos).getFloatTemperature(pos) - 0.4) * 50;
        final double dayNightVariance = averageTemperature * 0.05;

        return averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance);
    }

    /**
     * Q = mcT
     *
     * @param mass                 - KG
     * @param specificHeatCapacity - J/KG K
     * @param temperature          - K
     * @return Q, energy in joules
     */
    public static double getEnergyForTemperatureChange(final double mass, final double specificHeatCapacity, final double temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    /**
     * Q = mL
     *
     * @param mass               - KG
     * @param latentHeatCapacity - J/KG
     *
     * @return Q, energy in J
     */
    public static double getEnergyForStateChange(final double mass, final double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    /**
     * Gets the mass of an object from volume and density.
     *
     * @param volume  - in liters
     * @param density - in kg/m^3
     * @return
     */
    public static double getMass(final double volume, final double density) {
        return volume / 1000 * density;
    }

    /**
     * Mass (KG) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
     *
     * @param fluidStack - the fluid stack to get the mass of.
     *
     * @return The mass in KG
     */
    public static double getMass(final FluidStack fluidStack) {
        return getMass(fluidStack.amount, fluidStack.getFluid().getDensity());
    }

    /**
     * Default handler.

     @SubscribeEvent
     def thermalEventHandler(evt: ThermalEvent.EventThermalUpdate) {
     val pos = evt.position
     val block = pos.getBlock

     if (block == Blocks.flowing_water || block == Blocks.water) {
     if (evt.temperature >= 373) {
     ifl volume = FluidContainerRegistry.BUCKET_VOLUME * (evt.temperature / 373)
     MinecraftFo (FluidRegistry.getFluid("steam") != null) {
     varge.EVENT_BUS.post(new BoilEvent(pos.world, pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2))
     }
     }
     }

     if (block == Blocks.ice) {
     if (evt.temperature >= 273) {
     UpdateTicker.threaded.enqueue(() => pos.setBlock(Blocks.flowing_water))
     }
     }
     }*/
}