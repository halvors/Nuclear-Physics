package org.halvors.nuclearphysics.common.event.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.event.BoilEvent;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.utility.WorldUtility;

public class ThermalEventHandler {
    @SubscribeEvent
    public void onBoilEvent(final BoilEvent event) {
        final World world = event.getWorld();
        final int x = event.getX();
        final int y = event.getY();
        final int z = event.getZ();
        final Block block = world.getBlock(x, y, z);

        // Only boil water blocks.
        if (block == Blocks.water || block == Blocks.flowing_water) {
            // Boil the water into steam.
            for (int height = 1; height <= event.getMaxSpread(); height++) {
                final TileEntity tile = world.getTileEntity(x, y + height, z);

                if (tile instanceof IBoilHandler) {
                    IBoilHandler handler = (IBoilHandler) tile;
                    FluidStack fluidStack = event.getGas(height);

                    if (fluidStack.amount > 0 && handler.receiveGas(ForgeDirection.DOWN, fluidStack, false) > 0) {
                        fluidStack.amount -= handler.receiveGas(ForgeDirection.DOWN, fluidStack, true);
                    }
                }
            }

            // Randomly remove water blocks with not in controlled environment like a reactor.
            if (General.enableBoilingOfWaterBlocks && !event.isReactor() && world.rand.nextInt(1000) == 0) {
                world.setBlockToAir(x, y, z);
            }

            // Sound of lava flowing randomly plays when above temperature to boil water.
            if (world.rand.nextInt(2000) == 0) {
                world.playSoundEffect(x, y, z, "liquid.lava", 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
            }

            // Sounds of lava popping randomly plays when above temperature to boil water.
            if (world.rand.nextInt(4000) == 0) {
                world.playSoundEffect(x, y, z, "liquid.lavapop", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            }

            if (world.rand.nextInt(5) == 0) {
                WorldUtility.spawnParticle(world, "bubble", x + world.rand.nextFloat(), y + 0.5, z + world.rand.nextFloat(), 0, 0.05, 0);
            }

            if (world.rand.nextInt(50) == 0) {
                WorldUtility.spawnParticle(world, "cloud", x + world.rand.nextFloat(), y + 1.2, z + world.rand.nextFloat(), 0, 0.1, 0);
            }
        }
    }

    @SubscribeEvent
    public void onPlasmaSpawnEvent(final PlasmaSpawnEvent event) {
    	
        final World world = event.getWorld();
        final int x = event.getX();
        final int y = event.getY();
        final int z = event.getZ();
        final Block block = world.getBlock(x, y, z);

        if (!event.isCanceled()) {	// ??? and if not?
            if (block == Blocks.bedrock ||
                block == Blocks.iron_block) {
                event.setCanceled(true);
            }

            final TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof IElectromagnet) {
                event.setCanceled(true);
            }
        }
        if(!event.isCanceled()) {
        	world.setBlock(x, y, z, ModFluids.plasma.getBlock());
        }
    }

    @SubscribeEvent
    public void onThermalUpdateEvent(final ThermalUpdateEvent event) {
        final World world = event.getWorld();
        final int x = event.getX();
        final int y = event.getY();
        final int z = event.getZ();
        final Block block = world.getBlock(x, y, z);
        final TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof IElectromagnet) {
            event.setHeatLoss(event.getDeltaTemperature() * 0.6);
        }

        if (block.getMaterial().equals(Material.air)) {
            event.setHeatLoss(0.15);
        }

        if (block == Blocks.water ||
            block == Blocks.flowing_water) {
            if (event.getTemperature() >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
                final int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (event.getTemperature() / ThermalPhysics.WATER_BOIL_TEMPERATURE) * General.steamOutputMultiplier);

                MinecraftForge.EVENT_BUS.post(new BoilEvent(world, x, y, z, new FluidStack(FluidRegistry.WATER, volume), 2, event.isReactor()));

                event.setHeatLoss(0.2);
            }
        }

        if (block == Blocks.ice ||
            block == Blocks.packed_ice ||
            block == Blocks.snow) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlock(x, y, z, Blocks.flowing_water, 0, 3);
            }

            event.setHeatLoss(0.4);
        }

        if (block == Blocks.snow_layer) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlockToAir(x, y, z);
            }

            event.setHeatLoss(0.4);
        }
    }
}
