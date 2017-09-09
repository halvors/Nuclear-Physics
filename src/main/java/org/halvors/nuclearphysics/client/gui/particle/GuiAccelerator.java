package org.halvors.nuclearphysics.client.gui.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.particle.ContainerAccelerator;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.type.Color;

@SideOnly(Side.CLIENT)
public class GuiAccelerator extends GuiComponentContainer<TileParticleAccelerator> {
    public GuiAccelerator(InventoryPlayer inventoryPlayer, TileParticleAccelerator tile) {
        super(tile, new ContainerAccelerator(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 50));
        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 74));
        components.add(new GuiSlot(SlotType.NORMAL, this, 105, 74));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString(tile.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getName()) / 2), 6, 0x404040);

        BlockPos pos = tile.getPos().offset(tile.getFacing().getOpposite());
        String status;

        if (!EntityParticle.canSpawnParticle(tile.getWorld(), pos)) {
            status = Color.DARK_RED + "Fail to emit, try rotating.";
        } else if (tile.entityParticle != null && tile.velocity > 0) {
            status = Color.ORANGE + "Accelerating";
        } else {
            status = Color.DARK_GREEN + "Idle";
        }

        fontRendererObj.drawString("Velocity: " + Math.round((tile.velocity / TileParticleAccelerator.clientParticleVelocity) * 100) + "%", (xSize / 2) - 80, 27, 0x404040);
        fontRendererObj.drawString("Energy Used:", (xSize / 2) - 80, 38, 0x404040);
        fontRendererObj.drawString(UnitDisplay.getDisplay(tile.totalEnergyConsumed, UnitDisplay.Unit.JOULES), (xSize / 2) - 80, 49, 0x404040);

        //fontRendererObj.drawString(UnitDisplay.getDisplay(tile.acceleratorEnergyCostPerTick * 20, UnitDisplay.Unit.WATT), (xSize / 2) - 80, 60, 0x404040);
        //fontRendererObj.drawString(UnitDisplay.getDisplay(tile.getVoltageInput(null), UnitDisplay.Unit.VOLTAGE), (xSize / 2) - 80, 70, 0x404040);

        fontRendererObj.drawString("Antimatter: " + tile.antimatter + " mg", (xSize / 2) - 80, 80, 0x404040);
        fontRendererObj.drawString("Status:", (xSize / 2) - 80, 90, 0x404040);
        fontRendererObj.drawString(status, (xSize / 2) - 80, 100, 0x404040);
        fontRendererObj.drawString("Buffer: " + UnitDisplay.getDisplayShort(tile.getEnergyStorage().getEnergyStored(), UnitDisplay.Unit.JOULES) + "/" + UnitDisplay.getDisplayShort(tile.getEnergyStorage().getMaxEnergyStored(), UnitDisplay.Unit.JOULES), 8, 110, 0x404040);
        fontRendererObj.drawString("Facing: " + tile.getFacing().toString().toUpperCase(), 100, 123, 0x404040);

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}