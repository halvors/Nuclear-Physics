package org.halvors.quantum.atomic.client.gui.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.gui.GuiContainerBase;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.quantum.atomic.common.container.particle.ContainerAccelerator;
import org.halvors.quantum.atomic.common.entity.EntityParticle;
import org.halvors.quantum.atomic.common.tile.particle.TileAccelerator;
import org.halvors.quantum.atomic.common.utility.LanguageUtility;
import org.halvors.quantum.atomic.common.utility.energy.UnitDisplay;
import org.halvors.quantum.atomic.common.utility.transform.vector.Vector3;
import org.halvors.quantum.atomic.common.utility.type.Color;

@SideOnly(Side.CLIENT)
public class GuiAccelerator extends GuiContainerBase {
    private TileAccelerator tile;

    public GuiAccelerator(InventoryPlayer inventoryPlayer, TileAccelerator tile) {
        super(new ContainerAccelerator(inventoryPlayer, tile));

        this.tile = tile;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        String name = LanguageUtility.transelate("tile.machine." + EnumMachine.ACCELERATOR.ordinal() + ".name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        Vector3 position = new Vector3(tile);
        position.translate(tile.getFacing().getOpposite());

        String status;
        BlockPos pos = new BlockPos(position.getX(), position.getY(), position.getZ());

        if (!EntityParticle.canSpawnParticle(tile.getWorld(), pos)) {
            status = Color.DARK_RED + "Fail to emit; try rotating.";
        } else if (tile.entityParticle != null && tile.velocity > 0) {
            status = Color.ORANGE + "Accelerating";
        } else {
            status = Color.DARK_GREEN + "Idle";
        }

        fontRendererObj.drawString("Velocity: " + Math.round((tile.velocity / TileAccelerator.clientParticleVelocity) * 100) + "%", 8, 27, 0x404040);
        fontRendererObj.drawString("Energy Used:", 8, 38, 0x404040);
        fontRendererObj.drawString(UnitDisplay.getDisplay(tile.totalEnergyConsumed, UnitDisplay.Unit.JOULES), 8, 49, 0x404040);
        fontRendererObj.drawString(UnitDisplay.getDisplay(tile.acceleratorEnergyCostPerTick * 20, UnitDisplay.Unit.WATT), 8, 60, 0x404040);
        //fontRendererObj.drawString(UnitDisplay.getDisplay(tile.getVoltageInput(null), UnitDisplay.Unit.VOLTAGE), 8, 70, 0x404040);
        fontRendererObj.drawString("Antimatter: " + tile.antimatter + " mg", 8, 80, 0x404040);
        fontRendererObj.drawString("Status:", 8, 90, 0x404040);
        fontRendererObj.drawString(status, 8, 100, 0x404040);
        fontRendererObj.drawString("Buffer: " + UnitDisplay.getDisplayShort(tile.getEnergyStorage().getEnergyStored(), UnitDisplay.Unit.JOULES) + "/" + UnitDisplay.getDisplayShort(tile.getEnergyStorage().getMaxEnergyStored(), UnitDisplay.Unit.JOULES), 8, 110, 0x404040);
        fontRendererObj.drawString("Facing: " + tile.getFacing().toString().toUpperCase(), 100, 123, 0x404040);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawSlot(131, 25);
        drawSlot(131, 50);
        drawSlot(131, 74);
        drawSlot(105, 74);
    }
}