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
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

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
    public void onReact(ItemStack itemStack, IReactor reactor) {
        TileEntity tileEntity = (TileEntity) reactor;
        World world = tileEntity.getWorld();
        int reactors = 0;

        for (int side = 0; side < 6; side++) {
            Vector3 checkPos = new Vector3(tileEntity).translate(EnumFacing.getFront(side));
            TileEntity tile = checkPos.getTileEntity(world);

            // Check that the other reactors not only exist but also are running.
            if (tile instanceof IReactor && ((IReactor) tile).getTemperature() > breedingTemp) {
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
            if (reactor.getWorld().getWorldTime() % 20 == 0) {
                itemStack.setItemDamage(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDamage()));
            }

            // Create toxic waste.
            if (ConfigurationManager.General.allowToxicWaste && world.rand.nextFloat() > 0.5) {
                FluidStack fluid = QuantumFluids.fluidStackToxicWaste.copy();
                fluid.amount = 1;

                reactor.getTank().fill(fluid, true);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        super.getSubItems(item, tabs, list);

        list.add(new ItemStack(item, 1, getMaxDamage() - 1));
    }
}