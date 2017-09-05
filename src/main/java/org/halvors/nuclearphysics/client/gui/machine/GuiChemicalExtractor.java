package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiComponentContainer<TileChemicalExtractor> {
    public GuiChemicalExtractor(InventoryPlayer inventory, TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        components.add(new GuiSlot(SlotType.BATTERY, this, 79, 49));
        components.add(new GuiSlot(SlotType.NORMAL, this, 52, 24, "Input slot"));
        components.add(new GuiSlot(SlotType.NORMAL, this, 106, 24));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 75, 24));
        components.add(new GuiFluidGauge(tile::getInputTank, this, 8, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 49));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getName()) / 2), 6, 0x404040);

        //renderUniversalDisplay(100, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);
        //renderUniversalDisplay(8, 112, TileChemicalExtractor.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), 9, 85 + i * 9, 0x404040);
        }

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
