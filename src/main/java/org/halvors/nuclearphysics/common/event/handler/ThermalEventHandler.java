package org.halvors.nuclearphysics.common.event.handler;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.event.BoilEvent;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.grid.IUpdate;
import org.halvors.nuclearphysics.common.grid.UpdateTicker;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;

public class ThermalEventHandler {
    @SubscribeEvent
    public void onBoilEvent(BoilEvent event) {
        for (int height = 1; height <= event.getMaxSpread(); height++) {
            final TileEntity tile = event.getWorld().getTileEntity(event.getPos().up(height));

            if (tile != null && tile.hasCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                final IBoilHandler boilHandler = tile.getCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN);
                final FluidStack fluidStack = event.getRemainForSpread(height);

                if (fluidStack.amount > 0 && boilHandler.receiveGas(fluidStack, false) > 0) {
                    fluidStack.amount -= boilHandler.receiveGas(fluidStack, true);
                }
            }
        }

        /*
        final Block block = event.getWorld().getBlockState(event.getPos()).getBlock();

        // Reactors will not actually remove water source blocks, however weapons will.
        if ((block == Blocks.water ||block == Blocks.flowing_water) && position.getBlockMetadata(event.world) == 0 && !event.isReactor) {
            position.setBlock(event.world, Blocks.air);
        }

        event.setResult(Result.DENY);
        */
    }

    @SubscribeEvent
    public void onPlasmaSpawnEvent(PlasmaSpawnEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();

        if (block == Blocks.BEDROCK || block == Blocks.IRON_BLOCK) {
            return;
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasma) {
            TilePlasma tilePlasma = (TilePlasma) tile;
            tilePlasma.setTemperature(event.getTemperature());

            return;
        }

        if (tile instanceof IElectromagnet) {
            return;
        }

        // Replacing block with plasma.
        world.setBlockState(pos, ModFluids.plasma.getBlock().getDefaultState());

        // We need to update the tile entity with the one from the plasma block that didn't exist before.
        tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasma) {
            TilePlasma tilePlasma = (TilePlasma) tile;
            tilePlasma.setTemperature(event.getTemperature());
        }
    }

    @SubscribeEvent
    public void onThermalUpdateEvent(ThermalUpdateEvent event) {
        final World world = (World) event.getWorld();
        final BlockPos pos = event.getPos();
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();

        if (block == ModBlocks.blockElectromagnet) {
            event.heatLoss = event.deltaTemperature * 0.6F;
        }

        // TODO: Synchronized maybe not reqiured for all the following code?
        synchronized (world) {
            if (state.getMaterial().equals(Material.AIR)) {
                event.heatLoss = 0.15F;
            }

            if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                if (event.temperature >= ThermalPhysics.waterBoilTemperature) {
                    int volume = (int) (Fluid.BUCKET_VOLUME * (event.temperature / ThermalPhysics.waterBoilTemperature) * ConfigurationManager.General.steamOutputMultiplier);

                    MinecraftForge.EVENT_BUS.post(new BoilEvent(world, pos, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(ModFluids.steam, volume), 2, event.isReactor));

                    // Sound of lava flowing randomly plays when above temperature to boil water.
                    if (world.rand.nextInt(80) == 0) {
                        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
                    }

                    // Sounds of lava popping randomly plays when above temperature to boil water.
                    if (world.rand.nextInt(40) == 0) {
                        world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                    }

                    if (world.rand.nextInt(20) == 0) {
                        ((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.2, pos.getZ() + world.rand.nextFloat(), 0, 0, 1, 0, 0.1);
                    }

                    if (world.rand.nextInt(5) == 0) {
                        ((WorldServer) world).spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + world.rand.nextFloat(), pos.getY() + 0.5, pos.getZ() + world.rand.nextFloat(), 0, 0, 1, 0, 0.05);
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
