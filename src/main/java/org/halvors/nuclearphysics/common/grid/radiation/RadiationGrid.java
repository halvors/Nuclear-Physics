package org.halvors.nuclearphysics.common.grid.radiation;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.event.ThermalEvent;
import org.halvors.nuclearphysics.common.grid.IUpdate;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.type.Pair;

import java.util.HashMap;
import java.util.Map;

public class RadiationGrid implements IUpdate {
    private static final HashMap<Pair<World, BlockPos>, Float> radiationSource = new HashMap<>();

    private static final float spread = 1 / 7F;
    private static final float deltaTime = 1 / 20F;

    private static final float backgroundRadiation = (float) Math.pow(0.000012373419479169309221013910322359, -6);

    public static void addRadiation(World world, BlockPos pos, float deltaRadiation) {

        float original = radiationSource.getOrDefault(new Pair<>(world, pos), backgroundRadiation);
        float newRadiation = original + deltaRadiation;

        if (Math.abs(newRadiation - backgroundRadiation) > 0.4) {
            radiationSource.put(new Pair<>(world, pos), original + deltaRadiation);
        } else {
            radiationSource.remove(new Pair<>(world, pos));
        }
    }

    public static float getRadiation(World world, BlockPos pos) {
        if (radiationSource.containsKey(new Pair<>(world, pos))) {
            return radiationSource.get(new Pair<>(world, pos));
        }

        return backgroundRadiation;
    }

    @Override
    public void update() {
        for (Map.Entry<Pair<World, BlockPos>, Float> entry : new HashMap<>(radiationSource).entrySet()) {
            // Distribute radiation.
            final World world = entry.getKey().getLeft();
            final BlockPos pos = entry.getKey().getRight();

            // Deal with different block types.
            float currentRadiation = getRadiation(world, pos);

            if (currentRadiation < 0) {
                radiationSource.remove(new Pair<>(world, pos));
            } else {
                float deltaFromEquilibrium = backgroundRadiation - currentRadiation;

                final TileEntity tile = world.getTileEntity(pos);
                final TileEntity tileUp = world.getTileEntity(pos.up());
                boolean isReactor = tile instanceof IReactor || tileUp != null && tileUp.hasCapability(CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY, EnumFacing.DOWN);

                ThermalEvent.ThermalUpdateEvent event = new ThermalEvent.ThermalUpdateEvent(world, pos, currentRadiation, deltaFromEquilibrium, deltaTime, isReactor);
                MinecraftForge.EVENT_BUS.post(event);

                addRadiation(world, pos, (deltaFromEquilibrium > 0 ? 1 : -1) * Math.min(Math.abs(deltaFromEquilibrium), Math.abs(event.getHeatLoss())));

                // Spread radiation to surrounding.
                for (EnumFacing side : EnumFacing.values()) {
                    BlockPos adjacentPos = pos.offset(side);

                    float deltaRadiation = getRadiation(world, pos) - getRadiation(world, adjacentPos);
                    Material adjacentMaterial = world.getBlockState(adjacentPos).getBlock().getBlockState().getBaseState().getMaterial();
                    float deltaSpread = (adjacentMaterial.isSolid() ? spread : spread / 2) * deltaTime;

                    if (deltaRadiation > 0) {
                        addRadiation(world, adjacentPos, deltaRadiation * deltaSpread);
                        addRadiation(world, pos, -deltaRadiation * deltaSpread);
                    }
                }
            }
        }
    }

    public boolean canUpdate() {
        return !NuclearPhysics.getProxy().isPaused();
    }

    public boolean continueUpdate() {
        return true;
    }
}
