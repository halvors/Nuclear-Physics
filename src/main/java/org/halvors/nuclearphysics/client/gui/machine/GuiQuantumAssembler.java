package org.halvors.nuclearphysics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.common.container.machine.ContainerQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

@SideOnly(Side.CLIENT)
public class GuiQuantumAssembler extends GuiMachine<TileQuantumAssembler> {
    public GuiQuantumAssembler(final InventoryPlayer inventoryPlayer, final TileQuantumAssembler tile) {
        super(tile, new ContainerQuantumAssembler(inventoryPlayer, tile));

        defaultResource = ResourceUtility.getResource(EnumResource.GUI, "quantum_assembler.png");
        ySize = 230;
        titleOffset = -7;

        components.add(new GuiSlot(this, 79, 39));
        components.add(new GuiSlot(this, 52, 55));
        components.add(new GuiSlot(this, 106, 55));
        components.add(new GuiSlot(this, 52, 87));
        components.add(new GuiSlot(this, 106, 87));
        components.add(new GuiSlot(this, 79, 102));
        components.add(new GuiSlot(this, 79, 71));
    }

    @Override
    public void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String displayText;

        if (tile.getOperatingTicks() > 0) {
            displayText = LanguageUtility.transelate("gui.machine") + ": " + (int) (((float) tile.getOperatingTicks() / (float) TileQuantumAssembler.TICKS_REQUIRED) * 100) + "%";
        } else if (tile.canProcess()) {
            displayText = LanguageUtility.transelate("gui.ready");
        } else {
            displayText = LanguageUtility.transelate("gui.idle");
        }

        fontRendererObj.drawString(displayText, (xSize / 2) - 80, ySize - 106, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}