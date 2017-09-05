package org.halvors.nuclearphysics.client.gui.modular.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class GuiNuclearBoiler extends GuiComponentContainer<TileNuclearBoiler> {
    public GuiNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(tile, new ContainerNuclearBoiler(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.BATTERY, this, 55, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 80, 25));
        components.add(new GuiProgress(this, 110, 26, tile.timer / TileNuclearBoiler.tickTime));
        components.add(new GuiFluidGauge(tile::getInputTank, this, 8, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
        components.add(new GuiSlot(SlotType.GAS, this, 134, 49));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getName()) / 2), 6, 0x404040);

        //renderUniversalDisplay(8, 112, TileNuclearBoiler.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(110, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The nuclear boiler can boil", 8, 75, 0x404040);
        fontRendererObj.drawString("yellow cake into uranium", 8, 85, 0x404040);
        fontRendererObj.drawString("hexafluoride gas to be refined.", 8, 95, 0x404040);

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}