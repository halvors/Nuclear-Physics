package org.halvors.nuclearphysics.client.gui.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.particle.ContainerParticleAccelerator;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class GuiParticleAccelerator extends GuiMachine<TileParticleAccelerator> {
    public GuiParticleAccelerator(InventoryPlayer inventoryPlayer, TileParticleAccelerator tile) {
        super(tile, new ContainerParticleAccelerator(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 50));
        components.add(new GuiSlot(SlotType.NORMAL, this, 131, 74));
        components.add(new GuiSlot(SlotType.NORMAL, this, 105, 74));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        BlockPos pos = tile.getPos().offset(tile.getFacing().getOpposite());
        String status;

        if (!EntityParticle.canSpawnParticle(tile.getWorld(), pos)) {
            status = Color.DARK_RED + LanguageUtility.transelate("gui.failedToEmitTryRotating");
        } else if (tile.getEntityParticle() != null && tile.getVelocity() > 0) {
            status = Color.ORANGE + LanguageUtility.transelate("gui.accelerating");
        } else {
            status = Color.DARK_GREEN + LanguageUtility.transelate("gui.idle");
        }

        fontRendererObj.drawString(LanguageUtility.transelate("gui.velocity") + ": " + Math.round((tile.getVelocity() / TileParticleAccelerator.antimatterCreationSpeed) * 100) + "%", (xSize / 2) - 80, (ySize / 2) - 80, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.storedAntimatter") + ": " + tile.getAntimatterCount() + " mg", (xSize / 2) - 80, (ySize / 2) - 68, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.energyUsed") + ": " + UnitDisplay.getEnergyDisplay(tile.totalEnergyConsumed), (xSize / 2) - 80, (ySize / 2) - 56, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.status") + ": ", (xSize / 2) - 80, (ySize / 2) - 20, 0x404040);
        fontRendererObj.drawString(status, (xSize / 2) - 80, (ySize / 2) - 8, 0x404040);
        //fontRendererObj.drawString("Buffer: " + UnitDisplay.getEnergyDisplay(tile.getEnergyStorage().getEnergyStored()) + "/" + UnitDisplay.getEnergyDisplay(tile.getEnergyStorage().getMaxEnergyStored()), (xSize / 2) - 80, 110, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.facing") + ": " + tile.getFacing().toString().toUpperCase(), 100, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}