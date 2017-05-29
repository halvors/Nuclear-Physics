package org.halvors.atomicscience.old.fission;

import com.calclavia.core.api.atomicscience.IReactor;
import com.calclavia.core.api.atomicscience.IReactorComponent;
import com.calclavia.core.lib.transform.vector.Vector3;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.atomicscience.old.AtomicScience;
import org.halvors.atomicscience.old.Settings;

public class ItemFissileFuel extends ItemRadioactive implements IReactorComponent
{
    public static final int DECAY = 2500;
    public static final long ENERGY = 100000000000L;
    public static final long ENERGY_PER_TICK = 2000000L;

    public ItemFissileFuel()
    {
        setMaxStackSize(1);
        setMaxDurability(2500);
        setNoRepair();
        setNoRepair();
    }

    @Override
    public void onReact(ItemStack itemStack, IReactor reactor)
    {
        TileEntity tileEntity = (TileEntity) reactor;
        World world = tileEntity.getWorld();

        float wenDu = 0.0F;
        int reactors = 0;

        for (int i = 0; i < 6; i++) {
            Vector3 checkPos = new Vector3(tileEntity).translate(ForgeDirection.getOrientation(i));
            TileEntity tile = checkPos.getTileEntity(world);
            if ((tile instanceof IReactor)) {
                reactors++;
            }
        }

        if (reactors >= 4) {
            float chance = Math.max((wenDu - 4000.0F) / 2000.0F, 0.0F);
            if (Math.random() <= chance) {
                itemStack.setMetadata(Math.max(itemStack.getMetadata() - 1, 0));
            }
        } else {
            reactor.heat(2000000L);
            if (reactor.world().getWorldTime() % 20L == 0L) {
                itemStack.setMetadata(Math.min(itemStack.getMetadata() + 1, itemStack.getMaxDurability()));
            }
        }

        if ((Settings.allowToxicWaste) && (world.rand.nextFloat() > 0.5D)) {
            FluidStack fluid = AtomicScience.FLUIDSTACK_TOXIC_WASTE.copy();
            fluid.amount = 1;
            reactor.fill(ForgeDirection.UNKNOWN, fluid, true);
        }
    }

    /* TODO: Fix this.
    @SideOnly(Side.CLIENT)
    public void func_77633_a(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, func_77612_l() - 1));
    }
    */
}
