package org.halvors.nuclearphysics.client.gui.modular.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.TextureUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class GuiFluidGauge extends GuiGauge<Fluid> {
    private IFluidInfoHandler infoHandler;

    public GuiFluidGauge(IFluidInfoHandler infoHandler, IGuiWrapper gui, int x, int y) {
        super(gui, ResourceUtility.getResource(ResourceType.GUI, "gui_base.png"), x, y);

        this.infoHandler = infoHandler;
    }

    @Override
    public int getRenderColor() {
        return infoHandler.getTank().getFluid().getFluid().getColor();
    }

    @Override
    public int getScaledLevel() {
        IFluidTank tank = infoHandler.getTank();

        if (tank.getFluidAmount() > 0 && tank.getFluid() != null) {
            return tank.getFluidAmount() * (height - 2) / tank.getCapacity();
        }

        return 0;
    }

    @Override
    public TextureAtlasSprite getIcon() {
        IFluidTank tank = infoHandler.getTank();

        return TextureUtility.getFluidTexture(tank.getFluid().getFluid(), TextureUtility.FluidType.STILL);
    }

    @Override
    public String getTooltipText() {
        IFluidTank tank = infoHandler.getTank();
        FluidStack fluidStack = tank.getFluid();

        return fluidStack != null ? fluidStack.getLocalizedName() + ": " + tank.getFluidAmount() : LanguageUtility.transelate("gui.noFluid");
    }

    public interface IFluidInfoHandler {
        FluidTank getTank();
    }
}
