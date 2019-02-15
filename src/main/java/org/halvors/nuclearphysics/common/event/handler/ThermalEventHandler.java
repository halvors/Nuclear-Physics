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
import org.halvors.nuclearphysics.api.BlockPos;
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
        final BlockPos pos = event.getPos();
        final Block block = pos.getBlock(world);

        // Only boil water blocks.
        if (block == Blocks.water || block == Blocks.flowing_water) {
            // Boil the water into steam.
            for (int height = 1; height <= event.getMaxSpread(); height++) {

                final TileEntity tile = pos.up(height).getTileEntity(world);

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
                pos.setBlockToAir(world);
            }

            // Sound of lava flowing randomly plays when above temperature to boil water.
            if (world.rand.nextInt(2000) == 0) {
                world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "liquid.lava", 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
            }

            // Sounds of lava popping randomly plays when above temperature to boil water.
            if (world.rand.nextInt(4000) == 0) {
                world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "liquid.lavapop", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            }

            if (world.rand.nextInt(5) == 0) {
                WorldUtility.spawnParticle(world, "bubble", pos.getX() + world.rand.nextFloat(), pos.getY() + 0.5, pos.getZ() + world.rand.nextFloat(), 0, 0.05, 0);
            }

            if (world.rand.nextInt(50) == 0) {
                WorldUtility.spawnParticle(world, "cloud", pos.getX() + world.rand.nextFloat(), pos.getY() + 1.2, pos.getZ() + world.rand.nextFloat(), 0, 0.1, 0);
            }
        }
    }

    @SubscribeEvent
    public void onPlasmaSpawnEvent(final PlasmaSpawnEvent event) {
    	
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final Block block = pos.getBlock(world);

        if (!event.isCanceled()) {
            // Checking if block is breakable, if not it's bedrock, portal, command block etc.
            if (pos.getBlockHardness(block, world) < 0 ||
                block == Blocks.iron_block) {
                event.setCanceled(true);
            }

            final TileEntity tile = pos.getTileEntity(world);

            if (tile instanceof IElectromagnet) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onThermalUpdateEvent(final ThermalUpdateEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final Block block = pos.getBlock(world);
        final TileEntity tile = pos.getTileEntity(world);

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
                MinecraftForge.EVENT_BUS.post(new BoilEvent(world, pos, new FluidStack(FluidRegistry.WATER, volume), 2, event.isReactor()));

                event.setHeatLoss(0.2);
            }
        }

        if (block == Blocks.ice ||
            block == Blocks.packed_ice ||
            block == Blocks.snow) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                pos.setBlock(Blocks.flowing_water, 0, 3, world);
            }

            event.setHeatLoss(0.4);
        }

        if (block == Blocks.snow_layer) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                pos.setBlockToAir(world);
            }

            event.setHeatLoss(0.4);
        }
    }
}
