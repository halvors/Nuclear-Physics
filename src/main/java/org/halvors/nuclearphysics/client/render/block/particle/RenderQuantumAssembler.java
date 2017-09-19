package org.halvors.nuclearphysics.client.render.block.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.particle.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.Arrays;
import java.util.Collections;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends RenderTile<TileQuantumAssembler> {
    private static final OBJModelContainer modelPartHands = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "quantum_assembler.obj"), Arrays.asList("BackArmLower", "BackArmUpper", "FrontArmLower", "FrontArmUpper", "LeftArmLower", "LeftArmUpper", "RightArmLower", "RightArmUpper"));
    private static final OBJModelContainer modelPartArms = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "quantum_assembler.obj"), Arrays.asList("MiddleRotor", "MiddleRotorArmBase", "MiddleRotorFocusLaser", "MiddleRotorLowerArm", "MiddleRotorUpperArm"));
    private static final OBJModelContainer modelPartLargeArms = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "quantum_assembler.obj"), Arrays.asList("BottomRotor", "BottomRotorArmBase", "BottomRotorLowerArm", "BottomRotorResonatorArm", "BottomRotorUpperArm"));
    private static final OBJModelContainer modelPartResonanceCrystal = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "quantum_assembler.obj"), Collections.singletonList("ResonanceCrystal"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "quantum_assembler.obj"), Arrays.asList("Circuit1", "Circuit2", "Circuit3", "Circuit4", "ControlPad", "ControlPadRibbonCable", "ControlPadRibbonConnector", "MaterialPlinthBase", "MaterialPlinthCore", "MaterialPlinthStand", "PlinthBasePlate", "PlinthBaseRibbonConnector", "Ram1", "Ram2", "Ram3", "Ram4", "ResonatorAssembly", "ResonatorUnit", "SafetyGlassBack", "SafetyGlassFront", "SafetyGlassLeft", "SafetyGlassRight", "SafetyGlassTop"));
    private static final Render<EntityItem> itemRenderer = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem()) {
        @Override
        public boolean shouldBob() {
            return false;
        }
    };

    @Override
    public void render(TileQuantumAssembler tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(-tile.getRotationYaw1(), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartHands.render();
        modelPartResonanceCrystal.render();
        GlStateManager.popMatrix();

        // Small Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(tile.getRotationYaw2(), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartArms.render();
        GlStateManager.popMatrix();

        // Large Laser Arm.
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate(-tile.getRotationYaw3(), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPartLargeArms.render();
        GlStateManager.popMatrix();

        model.render();

        GlStateManager.popMatrix();

        // Render this in a new context.
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 0.3, z + 0.5);

        // Render the item.
        if (tile.getEntityItem() != null) {
            itemRenderer.doRender(tile.getEntityItem(), 0, 0, 0, 0, -tile.getRotationYaw3());
        }
    }
}