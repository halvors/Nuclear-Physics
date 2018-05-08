package org.halvors.nuclearphysics.common.science.event.handler;

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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.science.event.FluidEvent;

@EventBusSubscriber
public class FluidEventHandler {
    @SubscribeEvent
    public static void onFluidEvaporateEvent(final FluidEvent.EvaporateEvent event) {
        final World world = event.getWorld();
        final BlockPos pos = event.getPos();
        final IBlockState state = world.getBlockState(pos);

        NuclearPhysics.getProxy().addScheduledTask(() -> {
            // Only boil water blocks.
            if (state == Blocks.WATER.getDefaultState() || state == Blocks.FLOWING_WATER.getDefaultState()) {
                // Boil the water into steam.
                for (int height = 1; height <= event.getMaxSpreadDistance(); height++) {
                    final TileEntity tile = world.getTileEntity(pos.up(height));

                    if (tile != null && tile.hasCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
                        final IBoilHandler boilHandler = tile.getCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN);
                        final FluidStack gasStack = event.getGas(height);

                        if (gasStack.amount > 0 && boilHandler.receiveGas(gasStack, false) > 0) {
                            gasStack.amount -= boilHandler.receiveGas(gasStack, true);
                        }
                    }
                }

                // Randomly remove water blocks with not in controlled environment like a reactor.
                if (ConfigurationManager.General.enableBoilingOfWaterBlocks && !event.isReactor() && world.rand.nextInt(1000) == 0) {
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
}
