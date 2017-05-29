package org.halvors.atomicscience.old.particle.accelerator;

import calclavia.lib.gui.GuiContainerBase;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;
import universalelectricity.api.vector.Vector3;

public class GuiAccelerator
        extends GuiContainerBase
{
    private TileAccelerator tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiAccelerator(InventoryPlayer par1InventoryPlayer, TileAccelerator tileEntity)
    {
        super(new ContainerAccelerator(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }

    public void func_74189_g(int x, int y)
    {
        this.field_73886_k.func_78276_b(this.tileEntity.func_70303_b(), 40, 10, 4210752);

        String status = "";
        Vector3 position = new Vector3(this.tileEntity);
        position.translate(this.tileEntity.getDirection().getOpposite());
        if (!EntityParticle.canCunZai(this.tileEntity.field_70331_k, position)) {
            status = "�4Fail to emit; try rotating.";
        } else if ((this.tileEntity.entityParticle != null) && (this.tileEntity.velocity > 0.0F)) {
            status = "�6Accelerating";
        } else {
            status = "�2Idle";
        }
        this.field_73886_k.func_78276_b("Velocity: " + Math.round(this.tileEntity.velocity / 0.9F * 100.0F) + "%", 8, 27, 4210752);
        this.field_73886_k.func_78276_b("Energy Used:", 8, 38, 4210752);
        this.field_73886_k.func_78276_b(UnitDisplay.getDisplay(this.tileEntity.yongDianLiang, UnitDisplay.Unit.JOULES), 8, 49, 4210752);
        this.field_73886_k.func_78276_b(UnitDisplay.getDisplay(9.6E7D, UnitDisplay.Unit.WATT), 8, 60, 4210752);
        this.field_73886_k.func_78276_b(UnitDisplay.getDisplay(this.tileEntity.getVoltageInput(null), UnitDisplay.Unit.VOLTAGE), 8, 70, 4210752);
        this.field_73886_k.func_78276_b("Antimatter: " + this.tileEntity.antimatter + " mg", 8, 80, 4210752);
        this.field_73886_k.func_78276_b("Status:", 8, 90, 4210752);
        this.field_73886_k.func_78276_b(status, 8, 100, 4210752);
        this.field_73886_k.func_78276_b("Buffer: " + UnitDisplay.getDisplayShort(this.tileEntity.energy.getEnergy(), UnitDisplay.Unit.JOULES) + "/" + UnitDisplay.getDisplayShort(this.tileEntity.energy.getEnergyCapacity(), UnitDisplay.Unit.JOULES), 8, 110, 4210752);
        this.field_73886_k.func_78276_b("Facing: " + this.tileEntity.getDirection().getOpposite(), 100, 123, 4210752);
    }

    protected void func_74185_a(float par1, int x, int y)
    {
        super.func_74185_a(par1, x, y);

        drawSlot(131, 25);
        drawSlot(131, 50);
        drawSlot(131, 74);
        drawSlot(105, 74);
    }
}
