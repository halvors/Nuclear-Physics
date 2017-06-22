package org.halvors.quantum.common.event;

import cpw.mods.fml.common.eventhandler.Event;
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
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.block.reactor.fusion.IElectromagnet;
import org.halvors.quantum.common.event.ThermalEvent.ThermalUpdateEvent;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.grid.UpdateTicker;
import org.halvors.quantum.lib.thermal.IBoilHandler;
import org.halvors.quantum.lib.thermal.ThermalPhysics;
import universalelectricity.api.net.IUpdate;

public class ThermalEventHandler {
    @SubscribeEvent
    public void onBoilEvent(BoilEvent event) {
        Vector3 position = event.getPosition();
        Block block = position.getBlock(event.world);

        for (int height = 1; height <= event.getMaxSpread(); height++) {
            TileEntity tileEntity = event.world.getTileEntity(position.intX(), position.intY() + height, position.intZ());

            if (tileEntity instanceof IBoilHandler) {
                IBoilHandler handler = (IBoilHandler) tileEntity;
                FluidStack fluid = event.getRemainForSpread(height);

                if (fluid.amount > 0) {
                    if (handler.canFill(ForgeDirection.DOWN, fluid.getFluid())) {
                        fluid.amount -= handler.fill(ForgeDirection.DOWN, fluid, true);
                    }
                }
            }
        }

        /*
        // Reactors will not actually remove water source blocks, however weapons will.
        if ((block == Blocks.water ||block == Blocks.flowing_water) && position.getBlockMetadata(event.world) == 0 && !event.isReactor) {
            position.setBlock(event.world, Blocks.air);
        }
        */

        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void onPlasmaSpawnEvent(PlasmaEvent.PlasmaSpawnEvent event) {
        Vector3 position = new Vector3(event.x, event.y, event.z);
        Block block = position.getBlock(event.world);

        if (block != null) {
            TileEntity tile = position.getTileEntity(event.world);

            if (block == Blocks.bedrock || block == Blocks.iron_block) {
                return;
            }

            if (tile instanceof TilePlasma) {
                ((TilePlasma) tile).setTemperature(event.temperature);

                return;
            }

            if (tile instanceof IElectromagnet) {
                return;
            }
        }

        position.setBlock(event.world, Quantum.blockPlasma);

        TileEntity tile = position.getTileEntity(event.world);

        if (tile instanceof TilePlasma) {
            ((TilePlasma) tile).setTemperature(event.temperature);
        }
    }

    @SubscribeEvent
    public void onThermalUpdateEvent(ThermalUpdateEvent event) {
        final VectorWorld position = event.position;
        final World world = position.getWorld();
        Block block = position.getBlock();

        if (block == Quantum.blockElectromagnet) {
            event.heatLoss = event.deltaTemperature * 0.6F;
        }

        // TODO: Synchronized maybe not reqiured for all the following code?
        synchronized (world) {
            if (block.getMaterial() == Material.air) {
                event.heatLoss = 0.15F;
            }

            if (block == Blocks.water || block == Blocks.flowing_water) {
                if (event.temperature >= ThermalPhysics.waterBoilTemperature) {
                    if (FluidRegistry.getFluid("steam") != null) {
                        // TODO: INCORRECT!
                        int steamMultiplier = 1; // Add this as configuration option?
                        int volume = (int) (FluidContainerRegistry.BUCKET_VOLUME * (event.temperature / ThermalPhysics.waterBoilTemperature) * steamMultiplier);

                        MinecraftForge.EVENT_BUS.post(new BoilEvent(position.world, position, new FluidStack(FluidRegistry.WATER, volume), new FluidStack(FluidRegistry.getFluid("steam"), volume), 2, event.isReactor));
                    }

                    event.heatLoss = 0.2F;
                }
            }

            if (block == Blocks.ice || block == Blocks.snow || block == Blocks.snow_layer) {
                if (event.temperature >= ThermalPhysics.iceMeltTemperature) {
                    if (block == Blocks.ice || block == Blocks.snow) {
                        UpdateTicker.addNetwork(new IUpdate() {
                            @Override
                            public void update() {
                                position.setBlock(Blocks.flowing_water);
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
                    } else if (block == Blocks.snow_layer) {
                        UpdateTicker.addNetwork(new IUpdate() {
                            @Override
                            public void update() {
                                position.setBlock(Blocks.air);
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
                }

                event.heatLoss = 0.4F;
            }
        }
    }
}
