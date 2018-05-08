package org.halvors.nuclearphysics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.EnumSlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiMachine<TileChemicalExtractor> {
    public GuiChemicalExtractor(final InventoryPlayer inventory, final TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        components.add(new GuiSlot(EnumSlotType.BATTERY, this, 79, 49));
        components.add(new GuiSlot(EnumSlotType.NORMAL, this, 52, 24, LanguageUtility.transelate("tooltip.inputSlot")));
        components.add(new GuiSlot(this, 106, 24));
        components.add(new GuiProgress(() -> (double) tile.getOperatingTicks() / TileChemicalExtractor.TICKS_REQUIRED, this, 75, 24));
        components.add(new GuiFluidGauge(tile::getInputTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 24, 49));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 134, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 134, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
