package org.halvors.nuclearphysics.client.gui.process;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.process.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.process.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiMachine<TileChemicalExtractor> {
    public GuiChemicalExtractor(InventoryPlayer inventory, TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        components.add(new GuiSlot(SlotType.BATTERY, this, 79, 49));
        components.add(new GuiSlot(SlotType.NORMAL, this, 52, 24, LanguageUtility.transelate("tooltip.inputSlot")));
        components.add(new GuiSlot(SlotType.NORMAL, this, 106, 24));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 75, 24));
        components.add(new GuiFluidGauge(tile::getInputTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 49));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRenderer.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
