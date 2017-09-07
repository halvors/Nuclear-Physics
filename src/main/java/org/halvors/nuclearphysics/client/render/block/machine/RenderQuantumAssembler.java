package org.halvors.nuclearphysics.client.render.block.machine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.Arrays;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends TileEntitySpecialRenderer<TileQuantumAssembler> {
    private static final OBJModelContainer modelPartHands = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("BackArmLower", "BackArmUpper", "FrontArmLower", "FrontArmUpper", "LeftArmLower", "LeftArmUpper", "RightArmLower", "RightArmUpper"));
    private static final OBJModelContainer modelPartArms = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("MiddleRotor", "MiddleRotorArmBase", "MiddleRotorFocusLaser", "MiddleRotorLowerArm", "MiddleRotorUpperArm"));
    private static final OBJModelContainer modelPartLargeArms = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("BottomRotor", "BottomRotorArmBase", "BottomRotorLowerArm", "BottomRotorResonatorArm", "BottomRotorUpperArm"));
    private static final OBJModelContainer modelPartResonanceCrystal = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Collections.singletonList("ResonanceCrystal"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("Circuit1", "Circuit2", "Circuit3", "Circuit4", "ControlPad", "ControlPadRibbonCable", "ControlPadRibbonConnector", "MaterialPlinthBase", "MaterialPlinthCore", "MaterialPlinthStand", "PlinthBasePlate", "PlinthBaseRibbonConnector", "Ram1", "Ram2", "Ram3", "Ram4", "ResonatorAssembly", "ResonatorUnit", "SafetyGlassBack", "SafetyGlassFront", "SafetyGlassLeft", "SafetyGlassRight", "SafetyGlassTop"));
    private static final Render<EntityItem> renderItem = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(EntityItem.class);

    @Override
    public void renderTileEntityAt(TileQuantumAssembler tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(-tile.rotationYaw1, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartHands.render();
        modelPartResonanceCrystal.render();
        GlStateManager.popMatrix();

        // Small Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(tile.rotationYaw2, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartArms.render();
        GlStateManager.popMatrix();

        // Large Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(-tile.rotationYaw3, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartLargeArms.render();
        GlStateManager.popMatrix();

        // Render the item.
        GlStateManager.pushMatrix();

        if (tile.entityItem != null) {
            renderItem.doRender(tile.entityItem, x + 0.5, y + 0.2, z + 0.5, 0, 0);
        }

        GlStateManager.popMatrix();

        model.render();

        GlStateManager.popMatrix();
    }
}