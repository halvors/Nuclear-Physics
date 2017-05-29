package org.halvors.atomicscience.old.process.fission;

import atomicscience.process.ContainerChemicalExtractor;
import atomicscience.process.TileChemicalExtractor;
import calclavia.lib.gui.GuiContainerBase;
import calclavia.lib.gui.GuiContainerBase.SlotType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiChemicalExtractor
        extends GuiContainerBase
{
    private TileChemicalExtractor tileEntity;

    public GuiChemicalExtractor(InventoryPlayer par1InventoryPlayer, TileChemicalExtractor tileEntity)
    {
        super(new ContainerChemicalExtractor(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    public void func_74189_g(int mouseX, int mouseY)
    {
        this.field_73886_k.func_78276_b(this.tileEntity.func_70303_b(), 45, 6, 4210752);

        renderUniversalDisplay(8, 112, 100000.0F, mouseX, mouseY, UnitDisplay.Unit.WATT);
        renderUniversalDisplay(100, 112, (float)this.tileEntity.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        this.field_73886_k.func_78276_b("The extractor can extract", 8, 75, 4210752);
        this.field_73886_k.func_78276_b("uranium, deuterium and tritium.", 8, 85, 4210752);
        this.field_73886_k.func_78276_b("Place them in the input slot.", 8, 95, 4210752);

        this.field_73886_k.func_78276_b(StatCollector.func_74838_a("container.inventory"), 8, this.field_74195_c - 96 + 2, 4210752);
        if ((func_74188_c(8, 18, this.meterWidth, this.meterHeight, mouseX, mouseY)) && (this.tileEntity.inputTank.getFluid() != null)) {
            if (this.tileEntity.inputTank.getFluid() != null) {
                drawTooltip(mouseX - this.field_74198_m, mouseY - this.field_74197_n + 10, new String[] { this.tileEntity.inputTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.inputTank.getFluid().amount + " L" });
            }
        }
        if ((func_74188_c(154, 18, this.meterWidth, this.meterHeight, mouseX, mouseY)) && (this.tileEntity.outputTank.getFluid() != null)) {
            if (this.tileEntity.outputTank.getFluid() != null) {
                drawTooltip(mouseX - this.field_74198_m, mouseY - this.field_74197_n + 10, new String[] { this.tileEntity.outputTank.getFluid().getFluid().getLocalizedName(), this.tileEntity.outputTank.getFluid().amount + " L" });
            }
        }
        if (((!func_74188_c(134, 49, 18, 18, mouseX, mouseY)) ||

                (this.tileEntity.func_70301_a(4) != null)) ||

                (func_74188_c(52, 24, 18, 18, mouseX, mouseY))) {
            if ((this.tileEntity.outputTank.getFluidAmount() > 0) && (this.tileEntity.func_70301_a(3) == null)) {
                drawTooltip(mouseX - this.field_74198_m, mouseY - this.field_74197_n + 10, new String[] { "Input slot" });
            }
        }
    }

    protected void func_74185_a(float par1, int x, int y)
    {
        super.func_74185_a(par1, x, y);

        drawSlot(79, 49, GuiContainerBase.SlotType.BATTERY);
        drawSlot(52, 24);
        drawSlot(106, 24);
        drawBar(75, 24, this.tileEntity.time / 280.0F);
        drawMeter(8, 18, this.tileEntity.inputTank.getFluidAmount() / this.tileEntity.inputTank.getCapacity(), this.tileEntity.inputTank.getFluid());
        drawSlot(24, 18, GuiContainerBase.SlotType.LIQUID);
        drawSlot(24, 49, GuiContainerBase.SlotType.LIQUID);
        drawMeter(154, 18, this.tileEntity.outputTank.getFluidAmount() / this.tileEntity.outputTank.getCapacity(), this.tileEntity.outputTank.getFluid());
        drawSlot(134, 18, GuiContainerBase.SlotType.LIQUID);
        drawSlot(134, 49, GuiContainerBase.SlotType.LIQUID);
    }
}
