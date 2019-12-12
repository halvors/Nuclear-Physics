package org.halvors.nuclearphysics.common.event.handler;

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.event.BoilEvent;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;

@EventBusSubscriber
public class ThermalEventHandler {
    @SubscribeEvent
    public static void onBoilEvent(final BoilEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);

        NuclearPhysics.getProxy().addScheduledTask(() -> {
            // Only boil water blocks.
            if (state == Blocks.WATER.getDefaultState() || state == Blocks.FLOWING_WATER.getDefaultState()) {
                // Boil the water into steam.
                for (int height = 1; height <= event.getMaxSpread(); height++) {
                    TileEntity tile = world.getTileEntity(pos.up(height));

                    if (tile != null && tile.hasCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                        IBoilHandler boilHandler = tile.getCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN);
                        FluidStack gasStack = event.getGas(height);

                        if (gasStack.amount > 0 && boilHandler.receiveGas(gasStack, false) > 0) {
                            gasStack.amount -= boilHandler.receiveGas(gasStack, true);
                        }
                    }
                }

                // Randomly remove water blocks with not in controlled environment like a reactor.
                if (General.enableBoilingOfWaterBlocks && !event.isReactor() && world.rand.nextInt(1000) == 0) {
                    world.setBlockToAir(pos);
                }

                // Sound of lava flowing randomly plays when above temperature to boil water.
                if (world.rand.nextInt(2000) == 0) {
                    world.playSound(null, pos, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
                }

                // Sounds of lava popping randomly plays when above temperature to boil water.
                if (world.rand.nextInt(4000) == 0) {
                    world.playSound(null, pos, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                }

                if (world.rand.nextInt(5) == 0) {
                    ((WorldServer) world).spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + world.rand.nextFloat(), pos.getY() + 0.5, pos.getZ() + world.rand.nextFloat(), 0, 0, 1, 0, 0.05);
                }

                if (world.rand.nextInt(50) == 0) {
                    ((WorldServer) world).spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.2, pos.getZ() + world.rand.nextFloat(), 0, 0, 1, 0, 0.1);
                }
            }
        }, world);
    }

    @SubscribeEvent
    public static void onPlasmaSpawnEvent(final PlasmaSpawnEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);

        if (!event.isCanceled()) {
            // Checking if block is breakable, if not it's bedrock, portal, command block etc.
            if (state.getBlockHardness(world, pos) < 0 ||
                state == Blocks.IRON_BLOCK.getDefaultState()) {
                event.setCanceled(true);
            }

            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof IElectromagnet) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onThermalUpdateEvent(ThermalUpdateEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IElectromagnet) {
            event.setHeatLoss(event.getDeltaTemperature() * 0.6);
        }

        final IBlockState state = world.getBlockState(pos);

        if (state.getMaterial() == Material.AIR) {
            event.setHeatLoss(0.15);
        }

        if (state == Blocks.WATER.getDefaultState() ||
            state == Blocks.FLOWING_WATER.getDefaultState()) {
            if (event.getTemperature() >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
                double volume = Fluid.BUCKET_VOLUME * (event.getTemperature() / ThermalPhysics.WATER_BOIL_TEMPERATURE) * General.steamOutputMultiplier;
                MinecraftForge.EVENT_BUS.post(new BoilEvent(world, pos, volume, 2, event.isReactor()));

                event.setHeatLoss(0.2);
            }
        }

        if (state == Blocks.ICE.getDefaultState() ||
            state == Blocks.PACKED_ICE.getDefaultState() ||
            state == Blocks.SNOW.getDefaultState()) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
            }

            event.setHeatLoss(0.4);
        }

        if (state == Blocks.SNOW_LAYER.getDefaultState()) {
            if (event.getTemperature() >= ThermalPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlockToAir(pos);
            }

            event.setHeatLoss(0.4);
        }
    }
}
