package org.halvors.nuclearphysics.client.render.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends RenderTile<TileQuantumAssembler> {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "quantum_assembler.obj"));
    private static final String[] modelPartHands = { "BackArmLower", "BackArmUpper", "FrontArmLower", "FrontArmUpper", "LeftArmLower", "LeftArmUpper", "RightArmLower", "RightArmUpper" };
    private static final String[] modelPartArms = { "MiddleRotor", "MiddleRotorArmBase", "MiddleRotorFocusLaser", "MiddleRotorLowerArm", "MiddleRotorUpperArm" };
    private static final String[] modelPartLargeArms = { "BottomRotor", "BottomRotorArmBase", "BottomRotorLowerArm", "BottomRotorResonatorArm", "BottomRotorUpperArm" };
    private static final String[] modelPartResonanceCrystal = { "ResonanceCrystal" };
    private static final RenderItem renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));

    public RenderQuantumAssembler() {
        super("quantum_assembler");
    }

    @Override
    protected void render(final TileQuantumAssembler tile, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(-tile.getRotationYaw1(), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        model.renderOnly(modelPartHands);
        model.renderOnly(modelPartResonanceCrystal);
        GL11.glPopMatrix();

        // Small Laser Arm.
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(tile.getRotationYaw2(), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        model.renderOnly(modelPartArms);
        GL11.glPopMatrix();

        // Large Laser Arm.
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated(-tile.getRotationYaw3(), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        model.renderOnly(modelPartLargeArms);
        GL11.glPopMatrix();

        model.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(ArrayUtils.addAll(modelPartHands, modelPartArms), modelPartLargeArms), modelPartResonanceCrystal));

        GL11.glPopMatrix();

        // Render this in a new context.
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.3, z + 0.5);

        // Render the item.
        if (tile.getEntityItem() != null) {
            renderItem.doRender(tile.getEntityItem(), 0, 0, 0, 0, -tile.getRotationYaw3());
        }
    }
}