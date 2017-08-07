package org.halvors.quantum.common.event;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.quantum.api.tile.IElectromagnet;
import org.halvors.quantum.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.quantum.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.quantum.common.grid.IUpdate;
import org.halvors.quantum.common.grid.UpdateTicker;
import org.halvors.quantum.common.grid.thermal.ThermalPhysics;
import org.halvors.quantum.common.init.QuantumBlocks;
import org.halvors.quantum.common.init.QuantumFluids;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;

public class ThermalEventHandler {
    @SubscribeEvent
    public void onBoilEvent(BoilEvent event) {
        for (int height = 1; height <= event.getMaxSpread(); height++) {
            final TileEntity tile = event.getWorld().getTileEntity(event.getPos().up(height));

            // TODO: Add custom capability?
            if (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                final IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
                //final IBoilHandler handler = (IBoilHandler) tileEntity;
                final FluidStack fluid = event.getRemainForSpread(height);

                if (fluid.amount > 0) {
                    //if (handler.canFill(EnumFacing.DOWN, fluid.getFluid())) {
                        fluid.amount -= handler.fill(fluid, true);
                    //}
                }
            }
        }

        /*
        final Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        // Reactors will not actually remove water source blocks, however weapons will.
        if ((block == Blocks.water ||block == Blocks.flowing_water) && position.getBlockMetadata(event.world) == 0 && !event.isReactor) {
            position.setBlock(event.world, Blocks.air);
        }
        */

        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void onPlasmaSpawnEvent(PlasmaSpawnEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final Block block = world.getBlockState(pos).getBlock();

        if (block != null) {
            final TileEntity tile = world.getTileEntity(pos);

            if (block == Blocks.BEDROCK || block == Blocks.IRON_BLOCK) {
                return;
            }

            if (tile instanceof TilePlasma) {
                ((TilePlasma) tile).setTemperature(event.getTemperature());

                return;
            }

            if (tile instanceof IElectromagnet) {
                return;
            }
        }

        if (block.isReplaceable(world, pos)) {
            world.setBlockState(pos, QuantumFluids.plasma.getBlock().getDefaultState());
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasma) {
            ((TilePlasma) tile).setTemperature(event.getTemperature());
        }
    }

    @SubscribeEvent
    public void onThermalUpdateEvent(ThermalUpdateEvent event) {
        final BlockPos pos = event.getPos();
        final World world = (World) event.getWorld();
        final Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        if (block == QuantumBlocks.blockElectromagnet) {
            event.heatLoss = event.deltaTemperature * 0.6F;
        }

        // TODO: Synchronized maybe not reqiured for all the following code?
        synchronized (world) {
            if (block.getDefaultState().getMaterial().equals(Material.AIR)) {
                event.heatLoss = 0.15F;
            }

            if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                if (event.temperature >= ThermalPhysics.waterBoilTemperature) {
                    Fluid fluidSteam = FluidRegistry.getFluid("steam");

                    if (fluidSteam != null) {
                        // TODO: INCORRECT!
                        int steamMultiplier = 1; // Add this as configuration option?
                        int volume = (int) (Fluid.BUCKET_VOLUME * (event.temperature / ThermalPhysics.waterBoilTemperature) * steamMultiplier);

                        MinecraftForge.EVENT_BUS.post(new BoilEvent(world, pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(fluidSteam, volume), 2, event.isReactor));
                    }

                    event.heatLoss = 0.2F;
                }
            }

            if (block == Blocks.ICE || block == Blocks.PACKED_ICE) {
                if (event.temperature >= ThermalPhysics.iceMeltTemperature) {
                    UpdateTicker.addNetwork(new IUpdate() {
                        @Override
                        public void update() {
                            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
                        }

                        @Override
                        public boolean canUpdate() {
                            return true;
                        }

                        @Override
                        public boolean continueUpdate() {
                            return false;
                        }
                    });
                }

                event.heatLoss = 0.4F;
            }

            if (block == Blocks.SNOW || block == Blocks.SNOW_LAYER) {
                if (event.temperature >= ThermalPhysics.iceMeltTemperature) {
                    UpdateTicker.addNetwork(new IUpdate() {
                        @Override
                        public void update() {
                            world.setBlockToAir(pos);
                        }

                        @Override
                        public boolean canUpdate() {
                            return true;
                        }

                        @Override
                        public boolean continueUpdate() {
                            return false;
                        }
                    });
                }

                event.heatLoss = 0.4F;
            }
        }
    }
}
