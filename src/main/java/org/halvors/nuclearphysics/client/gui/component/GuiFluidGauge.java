package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class GuiFluidGauge extends GuiGauge {
    private final IFluidInfoHandler fluidInfoHandler;

    public GuiFluidGauge(final IFluidInfoHandler fluidInfoHandler, final IGuiWrapper gui, final int x, final int y) {
        super(gui, x, y);

        this.fluidInfoHandler = fluidInfoHandler;
    }

    @Override
    protected int getScaledLevel() {
        final IFluidTank tank = fluidInfoHandler.getTank();

        if (tank.getFluidAmount() > 0 && tank.getFluid() != null) {
            return tank.getFluidAmount() * (HEIGHT - 2) / tank.getCapacity();
        }

        return 0;
    }

    @Override
    protected IIcon getTexture() {
        final FluidStack fluidStack = fluidInfoHandler.getTank().getFluid();

        if (fluidStack != null) {
            return fluidStack.getFluid().getIcon();
        }

        return null;
    }

    @Override
    protected String getTooltip() {
        final IFluidTank tank = fluidInfoHandler.getTank();
        final FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null && fluidStack.amount > 0) {
            return fluidStack.getLocalizedName() + ": " + tank.getFluidAmount() + " mB";
        } else {
            return LanguageUtility.transelate("tooltip.noFluid");
        }
    }
}
