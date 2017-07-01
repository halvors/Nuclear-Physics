package org.halvors.quantum.common.tile.reactor.fusion;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.quantum.common.event.PlasmaEvent;
import org.halvors.quantum.common.thermal.ThermalGrid;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

public class TilePlasma extends TileEntity implements ITickable {
    public static int plasmaMaxTemperature = 1000000;
    private int temperature = plasmaMaxTemperature;

    @Override
    public void update() {
        ThermalGrid.addTemperature(new VectorWorld(this), (temperature - ThermalGrid.getTemperature(new VectorWorld(this))) * 0.1F);

        if (world.getWorldTime() % 20 == 0) {
            temperature /= 1.5;

            if (temperature <= plasmaMaxTemperature / 10) {
                // At this temperature, set block to fire.
                world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);

                // TODO: Is this still needed?
                // We manually trigger a block update, to avoid client glitches.
                //world.markBlockForUpdate(xCoord, yCoord, zCoord);
            } else {
                for (EnumFacing side : EnumFacing.VALUES) {
                    // Randomize spread direction.
                    if (world.rand.nextFloat() < 0.4) {
                        BlockPos spreadPos = pos.offset(side);
                        TileEntity tileEntity = world.getTileEntity(spreadPos);

                        if (!(tileEntity instanceof TilePlasma)) {
                            MinecraftForge.EVENT_BUS.post(new PlasmaEvent.PlasmaSpawnEvent(world, pos, temperature));
                        }
                    }
                }
            }
        }
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
