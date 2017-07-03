package org.halvors.quantum.client.render.machine;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.render.OBJBakedModel;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

import java.util.Arrays;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends TileEntitySpecialRenderer<TileQuantumAssembler> {
    private static final OBJBakedModel modelPartHands = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("BackArmLower", "BackArmUpper", "FrontArmLower", "FrontArmUpper", "LeftArmLower", "LeftArmUpper", "RightArmLower", "RightArmUpper"));
    private static final OBJBakedModel modelPartArms = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("MiddleRotor", "MiddleRotorArmBase", "MiddleRotorFocusLaser", "MiddleRotorLowerArm", "MiddleRotorUpperArm"));
    private static final OBJBakedModel modelPartLargeArms = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("BottomRotor", "BottomRotorArmBase", "BottomRotorLowerArm", "BottomRotorResonatorArm", "BottomRotorUpperArm"));
    private static final OBJBakedModel modelPartResonanceCrystal = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Collections.singletonList("ResonanceCrystal"));
    private static final OBJBakedModel modelAll = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "quantum_assembler.obj"), Arrays.asList("Circuit1", "Circuit2", "Circuit3", "Circuit4", "ControlPad", "ControlPadRibbonCable", "ControlPadRibbonConnector", "MaterialPlinthBase", "MaterialPlinthCore", "MaterialPlinthStand", "PlinthBasePlate", "PlinthBaseRibbonConnector", "Ram1", "Ram2", "Ram3", "Ram4", "ResonatorAssembly", "ResonatorUnit", "SafetyGlassBack", "SafetyGlassFront", "SafetyGlassLeft", "SafetyGlassRight", "SafetyGlassTop"));

    @Override
    public void renderTileEntityAt(TileQuantumAssembler tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.disableRescaleNormal();

        /*
        if (tile.getWorld() != null) {
            RenderUtility.rotateBlockBasedOnDirection(tile.getDirection());
        }
        */

        GlStateManager.pushMatrix();
        GlStateManager.rotate(-tile.rotationYaw1, 0, 1, 0);
        modelPartHands.render();
        modelPartResonanceCrystal.render();
        GlStateManager.popMatrix();

        // Small Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.rotate(tile.rotationYaw2, 0, 1, 0);
        modelPartArms.render();
        GlStateManager.popMatrix();

        // Large Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-tile.rotationYaw3, 0, 1, 0);
        modelPartLargeArms.render();
        GlStateManager.popMatrix();

        modelAll.render();

        GlStateManager.popMatrix();

        // Render the item.
        //RenderItem renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));

        GlStateManager.pushMatrix();

        /*
        if (tile.entityItem != null) {
            renderItem.doRender(tile.entityItem, x + 0.5, y + 0.4, z + 0.5, 0, 0);
        }
        */

        GlStateManager.popMatrix();
    }
}