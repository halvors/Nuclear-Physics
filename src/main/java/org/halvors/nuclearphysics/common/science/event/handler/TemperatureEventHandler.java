package org.halvors.nuclearphysics.common.science.event.handler;

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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.event.BoilEvent;
import org.halvors.nuclearphysics.common.science.event.FluidEvent;
import org.halvors.nuclearphysics.common.science.event.TemperatureEvent;
import org.halvors.nuclearphysics.common.science.physics.ThermoPhysics;

@EventBusSubscriber
public class TemperatureEventHandler {
    @SubscribeEvent
    public static void onTemperatureUpdateEvent(final TemperatureEvent.UpdateEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IElectromagnet) {
            event.setHeatLoss(event.getDeltaTemperature() * 0.6);
        }

        final IBlockState state = world.getBlockState(pos);

        if (state.getMaterial() == Material.AIR) {
            event.setHeatLoss(0.15);
        }

        if (state == Blocks.WATER.getDefaultState() ||
                state == Blocks.FLOWING_WATER.getDefaultState()) {
            if (event.getTemperature() >= ThermoPhysics.WATER_BOIL_TEMPERATURE) {
                final int volume = (int) (Fluid.BUCKET_VOLUME * (event.getTemperature() / ThermoPhysics.WATER_BOIL_TEMPERATURE) * ConfigurationManager.General.steamOutputMultiplier);

                MinecraftForge.EVENT_BUS.post(new BoilEvent(world, pos, new FluidStack(FluidRegistry.WATER, volume), 2, event.isReactor()));

                event.setHeatLoss(0.2);
            }
        }

        if (state == Blocks.ICE.getDefaultState() ||
                state == Blocks.PACKED_ICE.getDefaultState() ||
                state == Blocks.SNOW.getDefaultState()) {
            if (event.getTemperature() >= ThermoPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
            }

            event.setHeatLoss(0.4);
        }

        if (state == Blocks.SNOW_LAYER.getDefaultState()) {
            if (event.getTemperature() >= ThermoPhysics.ICE_MELT_TEMPERATURE) {
                world.setBlockToAir(pos);
            }

            event.setHeatLoss(0.4);
        }
    }
}
