package org.halvors.quantum.client.render.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.lib.render.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends TileEntitySpecialRenderer {
    public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/quantumAssembler.obj"));
    public static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/quantumAssembler.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileQuantumAssembler) {
            TileQuantumAssembler tileQuantumAssembler = (TileQuantumAssembler) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);

            String[] hands = new String[] {"Back Arm Upper", "Back Arm Lower", "Right Arm Upper", "Right Arm Lower", "Front Arm Upper", "Front Arm Lower", "Left Arm Upper", "Left Arm Lower"};
            String[] arms = new String[] {"Middle Rotor Focus Lazer", "Middle Rotor Uppper Arm", "Middle Rotor Lower Arm", "Middle Rotor Arm Base", "Middle Rotor"};
            String[] largeArms = new String[] {"Bottom Rotor Upper Arm", "Bottom Rotor Lower Arm", "Bottom Rotor Arm Base", "Bottom Rotor", "Bottom Rotor Resonator Arm"};

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glRotatef(-tileQuantumAssembler.rotationYaw1, 0, 1, 0);
            model.renderOnly(hands);
            model.renderOnly("Resonance_Crystal");
            GL11.glPopMatrix();

            /** Small Laser Arm */
            GL11.glPushMatrix();
            GL11.glRotatef(tileQuantumAssembler.rotationYaw2, 0, 1, 0);
            model.renderOnly(arms);

            GL11.glPopMatrix();

            /** Large Laser Arm */
            GL11.glPushMatrix();
            GL11.glRotatef(-tileQuantumAssembler.rotationYaw3, 0, 1, 0);
            model.renderOnly(largeArms);
            GL11.glPopMatrix();

            model.renderAllExcept(ArrayUtils.add(ArrayUtils.addAll(ArrayUtils.addAll(hands, arms), largeArms), "Resonance_Crystal"));
            GL11.glPopMatrix();

            /** Render the item */
            RenderItem renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));

            GL11.glPushMatrix();

            if (tileQuantumAssembler.entityItem != null) {
                renderItem.doRender(tileQuantumAssembler.entityItem, x + 0.5F, y + 0.4F, z + 0.5F, 0, 0);
            }

            GL11.glPopMatrix();
        }
    }
}