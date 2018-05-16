package org.halvors.nuclearphysics.client.gui.particle;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiBar;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.common.container.particle.ContainerParticleAccelerator;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.tile.particle.TileParticleAccelerator;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class GuiParticleAccelerator extends GuiMachine<TileParticleAccelerator> {
    public GuiParticleAccelerator(final InventoryPlayer inventoryPlayer, final TileParticleAccelerator tile) {
        super(tile, new ContainerParticleAccelerator(inventoryPlayer, tile));

        final int centerX = xSize / 2;

        components.add(new GuiBar(tile.getEnergyStorage(), this, centerX - 80, 18));

        components.add(new GuiSlot(this, centerX + 39, 66));
        components.add(new GuiSlot(this, centerX + 62, 18));
        components.add(new GuiSlot(this, centerX + 62, 42));
        components.add(new GuiSlot(this, centerX + 62, 66));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        final BlockPos pos = tile.getPos().offset(tile.getFacing().getOpposite());
        final String status;

        if (!EntityParticle.canSpawnParticle(tile.getWorld(), pos)) {
            status = EnumColor.DARK_RED + LanguageUtility.transelate("gui.failedToEmitTryRotating");
        } else if (tile.getEntityParticle() != null && tile.getVelocity() > 0) {
            status = EnumColor.ORANGE + LanguageUtility.transelate("gui.accelerating");
        } else {
            status = EnumColor.DARK_GREEN + LanguageUtility.transelate("gui.idle");
        }

        fontRendererObj.drawString(LanguageUtility.transelate("gui.velocity") + ": " + Math.round((tile.getVelocity() / TileParticleAccelerator.ANTIMATTER_CREATION_SPEED) * 100) + "%", centerX - 70, (ySize / 2) - 85, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.storedAntimatter") + ": " + tile.getAntimatterCount() + " mg", centerX - 70, (ySize / 2) - 73, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.energyUsed") + ": " + UnitDisplay.getEnergyDisplay(tile.totalEnergyConsumed), centerX - 70, (ySize / 2) - 61, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.status") + ": ", centerX - 80, (ySize / 2) - 30, 0x404040);
        fontRendererObj.drawString(status, centerX - 80, (ySize / 2) - 18, 0x404040);
        fontRendererObj.drawString(LanguageUtility.transelate("gui.facing") + ": " + tile.getFacing().toString().toUpperCase(), 100, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}