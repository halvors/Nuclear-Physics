package org.halvors.atomicscience.old.fission.reactor;

import calclavia.lib.gui.GuiContainerBase;
import calclavia.lib.utility.LanguageUtility;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiReactorCell
        extends GuiContainerBase
{
    private TileReactorCell tileEntity;

    public GuiReactorCell(InventoryPlayer inventory, TileReactorCell tileEntity)
    {
        super(new ContainerReactorCell(inventory.field_70458_d, tileEntity));
        this.tileEntity = tileEntity;
    }

    public void func_74189_g(int x, int y)
    {
        this.field_73886_k.func_78276_b(this.tileEntity.func_70303_b(), this.field_74194_b / 2 - this.field_73886_k.func_78256_a(this.tileEntity.func_70303_b()) / 2, 6, 4210752);
        if (this.tileEntity.func_70301_a(0) != null)
        {
            this.field_73886_k.func_78276_b(LanguageUtility.getLocal("tooltip.temperature"), 9, 45, 4210752);
            this.field_73886_k.func_78276_b((int)this.tileEntity.temperature + "/" + 2000 + " K", 9, 58, 4210752);

            int secondsLeft = this.tileEntity.func_70301_a(0).func_77958_k() - this.tileEntity.func_70301_a(0).func_77960_j();
            this.field_73886_k.func_78276_b(LanguageUtility.getLocal("tooltip.remainingTime"), 100, 45, 4210752);
            this.field_73886_k.func_78276_b(secondsLeft + " seconds", 100, 58, 4210752);
        }
        this.field_73886_k.func_78276_b(LanguageUtility.getLocal("tooltip.remainingTime"), 100, 45, 4210752);
        if (func_74188_c(80, 40, this.meterWidth, this.meterHeight, x, y)) {
            if (this.tileEntity.tank.getFluid() != null) {
                drawTooltip(x - this.field_74198_m, y - this.field_74197_n + 10, new String[] { this.tileEntity.tank.getFluid().getFluid().getLocalizedName(), UnitDisplay.getDisplay(this.tileEntity.tank.getFluidAmount(), UnitDisplay.Unit.LITER) });
            } else {
                drawTooltip(x - this.field_74198_m, y - this.field_74197_n + 10, new String[] { "No Fluid" });
            }
        }
        List<String> desc = LanguageUtility.splitStringPerWord(LanguageUtility.getLocal("tile.atomicscience:reactorCell.tooltip"), 5);
        for (int i = 0; i < desc.size(); i++) {
            this.field_73886_k.func_78276_b((String)desc.get(i), 9, 85 + i * 9, 4210752);
        }
    }

    protected void func_74185_a(float par1, int x, int y)
    {
        super.func_74185_a(par1, x, y);
        drawSlot(78, 16);
        drawMeter(80, 36, this.tileEntity.tank.getFluidAmount() / this.tileEntity.tank.getCapacity(), this.tileEntity.tank.getFluid());
        if (this.tileEntity.func_70301_a(0) != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(64.0F, 0.0F, 0.0F);
            GL11.glScalef(0.5F, 1.0F, 1.0F);
            drawForce(20, 70, this.tileEntity.temperature / 2000.0F);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(136.0F, 0.0F, 0.0F);
            GL11.glScalef(0.5F, 1.0F, 1.0F);
            float ticksLeft = this.tileEntity.func_70301_a(0).func_77958_k() - this.tileEntity.func_70301_a(0).func_77960_j();
            drawElectricity(70, 70, ticksLeft / this.tileEntity.func_70301_a(0).func_77958_k());
            GL11.glPopMatrix();
        }
    }
}
