package org.halvors.nuclearphysics.common.item.reactor.fission;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;

import javax.annotation.Nonnull;

public class ItemFissileFuel extends ItemRadioactive implements IReactorComponent {
    public static final int decay = 2500;

    // Temperature at which the fuel rod will begin to re-enrich itself.
    public static final int breedingTemperature = 1200;

    // The energy in one KG of uranium is: 72PJ, 100TJ in one cell of uranium.
    public static final long energyDensity = 100000000000L;

    // Approximately 20,000,000J per tick. 400 MW.
    public static final long energyPerTick = energyDensity / 50000;

    public ItemFissileFuel() {
        super("fissile_fuel");

        setMaxStackSize(1);
        setMaxDamage(decay);
        setNoRepair();
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            items.add(new ItemStack(this, 1, getMaxDamage() - 1));
        }
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        TileEntity tile = (TileEntity) reactor;
        World world = tile.getWorld();
        int reactors = 0;

        for (EnumFacing side : EnumFacing.values()) {
            TileEntity checkTile = world.getTileEntity(tile.getPos().offset(side));

            // Check that the other reactors not only exist but also are running.
            if (checkTile instanceof IReactor && ((IReactor) checkTile).getTemperature() > breedingTemperature) {
                reactors++;
            }
        }

        // Only three reactor cells are required to begin the uranium breeding process instead of four.
        if (reactors >= 3) {
            // Breeding - Begin the process of re-enriching the uranium rod but not consistently.
            if (world.rand.nextInt(1000) <= 100 && reactor.getTemperature() > breedingTemperature) {
                // Cells can regain a random amount of health per tick.
                int healAmount = world.rand.nextInt(5);

                itemStack.setItemDamage(Math.max(itemStack.getMetadata() - healAmount, 0));
            }
        } else {
            // Fission - Begin the process of heating.
            reactor.heat(energyPerTick);

            // Consume fuel.
            if (world.getWorldTime() % 20 == 0) {
                itemStack.setItemDamage(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDamage()));
            }

            // Create toxic waste.
            if (ConfigurationManager.General.allowToxicWaste && world.rand.nextFloat() > 0.5) {
                reactor.getTank().fillInternal(new FluidStack(ModFluids.toxicWaste, 1), true);
            }
        }
    }
}