package org.halvors.quantum.common.item.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.api.IReactor;
import org.halvors.quantum.api.IReactorComponent;
import org.halvors.quantum.common.transform.vector.Vector3;

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
        super("fissileFuel");

        setMaxStackSize(1);
        setMaxDurability(decay);
        setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor) {
        TileEntity tileEntity = (TileEntity) reactor;
        World world = tileEntity.getWorld();
        int reactors = 0;

        for (int i = 0; i < 6; i++) {
            Vector3 checkPos = new Vector3(tileEntity).translate(ForgeDirection.getOrientation(i));
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

                itemStack.setMetadata(Math.max(itemStack.getMetadata() - healAmt, 0));
            }
        } else {
            // Fission - Begin the process of heating.
            reactor.heat(energyPerTick);

            // Consume fuel.
            if (reactor.getWorldObject().getWorldTime() % 20 == 0) {
                itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
            }

            // Create toxic waste.
            if (ConfigurationManager.General.allowToxicWaste && world.rand.nextFloat() > 0.5) {
                FluidStack fluid = Quantum.fluidStackToxicWaste.copy();
                fluid.amount = 1;

                reactor.fill(ForgeDirection.UNKNOWN, fluid, true);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, getMaxDurability() - 1));
    }
}