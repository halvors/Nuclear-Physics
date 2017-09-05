package org.halvors.nuclearphysics.client.gui.modular.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.TextureUtility;
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
            return TextureUtility.getFluidTexture(fluidStack.getFluid(), TextureUtility.FluidType.STILL);
        }

        return null;
    }

    @Override
    public String getTooltip() {
        IFluidTank tank = fluidInfoHandler.getTank();
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null) {
            if (fluidStack.amount > 0) {
                return fluidStack.getLocalizedName() + ": " + tank.getFluidAmount();
            } else {
                return fluidStack.getLocalizedName();
            }
        } else {
            return LanguageUtility.transelate("gui.noFluid");
        }
    }
}
