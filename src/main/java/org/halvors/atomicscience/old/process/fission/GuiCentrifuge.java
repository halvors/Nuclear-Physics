package org.halvors.atomicscience.old.process.fission;

import calclavia.lib.gui.GuiContainerBase;
import calclavia.lib.gui.GuiContainerBase.SlotType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiCentrifuge
        extends GuiContainerBase
{
    private TileCentrifuge tileEntity;

    public GuiCentrifuge(InventoryPlayer par1InventoryPlayer, TileCentrifuge tileEntity)
    {
        super(new ContainerCentrifuge(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    public void func_74189_g(int mouseX, int mouseY)
    {
        this.field_73886_k.func_78276_b(this.tileEntity.func_70303_b(), 60, 6, 4210752);

        String displayText = "";
        if (this.tileEntity.timer > 0) {
            displayText = "Processing";
        } else if (this.tileEntity.nengYong()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }
        this.field_73886_k.func_78276_b("Status: " + displayText, 70, 50, 4210752);

        renderUniversalDisplay(8, 112, 1000000.0F, mouseX, mouseY, UnitDisplay.Unit.WATT);
        renderUniversalDisplay(100, 112, (float)this.tileEntity.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        this.field_73886_k.func_78276_b("The centrifuge spins", 8, 75, 4210752);
        this.field_73886_k.func_78276_b("uranium hexafluoride gas into", 8, 85, 4210752);
        this.field_73886_k.func_78276_b("enriched uranium for fission.", 8, 95, 4210752);

        this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.inventory"), 8, this.field_74195_c - 96 + 2, 4210752);
        if ((func_74188_c(8, 18, this.meterWidth, this.meterHeight, mouseX, mouseY)) && (this.tileEntity.gasTank.getFluid() != null)) {
            drawTooltip(mouseX - this.field_74198_m, mouseY - this.field_74197_n + 10, new String[] { this.tileEntity.gasTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.gasTank.getFluid().amount + " L" });
        }
    }

    protected void func_74185_a(float par1, int x, int y)
    {
        super.func_74185_a(par1, x, y);

        drawSlot(80, 25);
        drawSlot(100, 25);
        drawSlot(130, 25, GuiContainerBase.SlotType.BATTERY);

        drawBar(40, 26, this.tileEntity.timer / 1200.0F);

        drawMeter(8, 18, this.tileEntity.gasTank.getFluidAmount() / this.tileEntity.gasTank.getCapacity(), this.tileEntity.gasTank.getFluid());
        drawSlot(24, 49, GuiContainerBase.SlotType.GAS);
    }
}
