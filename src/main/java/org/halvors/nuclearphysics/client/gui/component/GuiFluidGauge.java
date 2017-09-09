package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.event.TextureEventHandler.FluidType;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class GuiFluidGauge extends GuiGauge {
    private IFluidInfoHandler fluidInfoHandler;

    public GuiFluidGauge(IFluidInfoHandler fluidInfoHandler, IGuiWrapper gui, int x, int y) {
        super(gui, x, y);

        this.fluidInfoHandler = fluidInfoHandler;
    }

    @Override
    public int getScaledLevel() {
        IFluidTank tank = fluidInfoHandler.getTank();

        if (tank.getFluidAmount() > 0 && tank.getFluid() != null) {
            return tank.getFluidAmount() * (height - 2) / tank.getCapacity();
        }

        return 0;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        FluidStack fluidStack = fluidInfoHandler.getTank().getFluid();

        if (fluidStack != null) {
            return TextureEventHandler.getFluidTexture(fluidStack.getFluid(), FluidType.STILL);
        }

        return null;
    }

    @Override
    public String getTooltip() {
        IFluidTank tank = fluidInfoHandler.getTank();
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null && fluidStack.amount > 0) {
            return fluidStack.getLocalizedName() + ": " + tank.getFluidAmount() + " mB";
        } else {
            return LanguageUtility.transelate("gui.noFluid");
        }
    }
}
