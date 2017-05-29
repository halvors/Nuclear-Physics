package org.halvors.atomicscience.old.particle.quantum;

import calclavia.lib.gui.GuiContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay.Unit;

public class GuiQuantumAssembler
        extends GuiContainerBase
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("atomicscience", "textures/gui/gui_atomic_assembler.png");
    private TileQuantumAssembler tileEntity;
    private int containerWidth;
    private int containerHeight;

    public GuiQuantumAssembler(InventoryPlayer par1InventoryPlayer, TileQuantumAssembler tileEntity)
    {
        super(new ContainerQuantumAssembler(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
        this.field_74195_c = 230;
    }

    public void func_74189_g(int mouseX, int mouseY)
    {
        this.field_73886_k.func_78276_b(this.tileEntity.func_70303_b(), 65 - this.tileEntity.func_70303_b().length(), 6, 4210752);
        String displayText = "";
        if (this.tileEntity.shiJian > 0)
        {
            this.tileEntity.getClass();displayText = "Process: " + (int)(100.0F - this.tileEntity.shiJian / 2400.0F * 100.0F) + "%";
        }
        else if (this.tileEntity.canProcess())
        {
            displayText = "Ready";
        }
        else
        {
            displayText = "Idle";
        }
        this.field_73886_k.func_78276_b(displayText, 9, this.field_74195_c - 106, 4210752);
        renderUniversalDisplay(100, this.field_74195_c - 94, (float)this.tileEntity.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);
        renderUniversalDisplay(8, this.field_74195_c - 95, 1.0E10F, mouseX, mouseY, UnitDisplay.Unit.WATT);
    }

    protected void func_74185_a(float par1, int par2, int par3)
    {
        this.field_73882_e.field_71446_o.func_110577_a(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.containerWidth = ((this.field_73880_f - this.field_74194_b) / 2);
        this.containerHeight = ((this.field_73881_g - this.field_74195_c) / 2);

        func_73729_b(this.containerWidth, this.containerHeight, 0, 0, this.field_74194_b, this.field_74195_c);
    }
}
