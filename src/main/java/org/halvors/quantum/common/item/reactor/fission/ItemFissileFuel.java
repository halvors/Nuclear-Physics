package org.halvors.quantum.common.item.reactor.fission;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.init.QuantumFluids;
import org.halvors.quantum.common.item.ItemRadioactive;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFissileFuel extends ItemRadioactive implements IReactorComponent {
    public static final int decay = 2500;

    // Temperature at which the fuel rod will begin to re-enrich itself.
    public static final int breedingTemp = 1200;

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

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tabs, List<ItemStack> list) {
        list.add(new ItemStack(item));
        list.add(new ItemStack(item, 1, getMaxDamage() - 1));
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        TileEntity tile = (TileEntity) reactor;
        World world = tile.getWorld();
        int reactors = 0;

        for (EnumFacing side : EnumFacing.VALUES) {
            TileEntity checkTile = world.getTileEntity(tile.getPos().offset(side));

            // Check that the other reactors not only exist but also are running.
            if (checkTile instanceof IReactor && ((IReactor) checkTile).getTemperature() > breedingTemp) {
                reactors++;
            }
        }

        // Only three reactor cells are required to begin the uranium breeding process instead of four.
        if (reactors >= 3) {
            // Breeding - Begin the process of re-enriching the uranium rod but not consistently.
            if (world.rand.nextInt(1000) <= 100 && reactor.getTemperature() > breedingTemp) {
                // Cells can regain a random amount of health per tick.
                int healAmt = world.rand.nextInt(5);

                itemStack.setItemDamage(Math.max(itemStack.getMetadata() - healAmt, 0));
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
                FluidStack fluid = QuantumFluids.fluidStackToxicWaste.copy();
                fluid.amount = 1;

                reactor.getTank().fillInternal(fluid, true);
            }
        }
    }
}