package org.halvors.nuclearphysics.common.item.reactor.fission;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModFluids;

public class ItemFissileFuel extends ItemFuel implements IReactorComponent {
    // Temperature at which the fuel rod will begin to re-enrich itself.
    public static final int breedingTemperature = 1200;

    public ItemFissileFuel() {
        super("fissile_fuel");
    }

    @Override
    public void onReact(final ItemStack itemStack, final IReactor reactor) {
        final TileEntity tile = (TileEntity) reactor;
        final World world = tile.getWorld();
        int reactors = 0;

        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity checkTile = new BlockPos(tile).offset(side).getTileEntity(world);

            // Check that the other reactors not only exist but also are running.
            if (checkTile instanceof IReactor && ((IReactor) checkTile).getTemperature() > breedingTemperature) {
                reactors++;
            }
        }

        // Only three reactor cells are required to begin the uranium breeding machine instead of four.
        if (reactors >= 3) {
            // Breeding - Begin the machine of re-enriching the uranium rod but not consistently.
            if (world.rand.nextInt(1000) <= 100 && reactor.getTemperature() > breedingTemperature) {
                // Cells can regain a random amount of health per tick.
                final int healAmount = world.rand.nextInt(5);

                itemStack.setMetadata(Math.max(itemStack.getMetadata() - healAmount, 0));
            }
        } else {
            // Fission - Begin the machine of heating.
            reactor.heat(energyPerTick);

            // Consume fuel.
            if (world.getWorldTime() % 20 == 0) {
                itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
            }

            // Create toxic waste.
            if (General.allowToxicWaste && world.rand.nextFloat() > 0.5) {
                reactor.getTank().fill(new FluidStack(ModFluids.toxicWaste, 1), true);
            }
        }
    }
}