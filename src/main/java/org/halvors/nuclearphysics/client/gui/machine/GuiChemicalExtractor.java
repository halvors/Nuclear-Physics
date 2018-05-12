package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.*;
import org.halvors.nuclearphysics.client.gui.component.GuiBar.EnumBarType;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.EnumSlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiMachine<TileChemicalExtractor> {
    public GuiChemicalExtractor(final InventoryPlayer inventory, final TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        components.add(new GuiBar(tile.getEnergyStorage(), this, (xSize / 2) - 80, 18));
        components.add(new GuiFluidGauge(tile::getInputTank, this, (xSize / 2) - 70, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, (xSize / 2) - 54, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, (xSize / 2) - 54, 49));
        components.add(new GuiSlot(EnumSlotType.NORMAL, this, (xSize / 2) - 30, 24, LanguageUtility.transelate("tooltip.inputSlot")));

        components.add(new GuiBar(() -> tile.getOperatingTicks() / TileChemicalExtractor.TICKS_REQUIRED, EnumBarType.PROGRESS, this, (xSize / 2) - 6, 24));
        components.add(new GuiSlot(EnumSlotType.BATTERY, this, (xSize / 2) - 6, 49));

        components.add(new GuiSlot(this, (xSize / 2) + 22, 24));



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
