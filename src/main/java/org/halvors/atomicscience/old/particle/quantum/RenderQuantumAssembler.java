package org.halvors.atomicscience.old.particle.quantum;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler
        extends TileEntitySpecialRenderer
{
    public static final IModelCustom MODEL = AdvancedModelLoader.loadModel("/assets/atomicscience/models/quantumAssembler.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation("atomicscience", "models/quantumAssembler.png");
    private final RenderBlocks renderBlocks = new RenderBlocks();

    public void render(TileQuantumAssembler tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);

        String[] hands = { "Back Arm Upper", "Back Arm Lower", "Right Arm Upper", "Right Arm Lower", "Front Arm Upper", "Front Arm Lower", "Left Arm Upper", "Left Arm Lower" };
        String[] arms = { "Middle Rotor Focus Lazer", "Middle Rotor Uppper Arm", "Middle Rotor Lower Arm", "Middle Rotor Arm Base", "Middle Rotor" };
        String[] largeArms = { "Bottom Rotor Upper Arm", "Bottom Rotor Lower Arm", "Bottom Rotor Arm Base", "Bottom Rotor", "Bottom Rotor Resonator Arm" };

        func_110628_a(TEXTURE);

        GL11.glPushMatrix();
        GL11.glRotatef(-tileEntity.rotationYaw1, 0.0F, 1.0F, 0.0F);
        MODEL.renderOnly(hands);
        MODEL.renderOnly(new String[] { "Resonance_Crystal" });
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glRotatef(tileEntity.rotationYaw2, 0.0F, 1.0F, 0.0F);
        MODEL.renderOnly(arms);

        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glRotatef(-tileEntity.rotationYaw3, 0.0F, 1.0F, 0.0F);
        MODEL.renderOnly(largeArms);
        GL11.glPopMatrix();

        MODEL.renderAllExcept((String[])ArrayUtils.add(ArrayUtils.addAll(ArrayUtils.addAll(hands, arms), largeArms), "Resonance_Crystal"));
        GL11.glPopMatrix();

        RenderItem renderItem = (RenderItem)RenderManager.field_78727_a.func_78715_a(EntityItem.class);

        GL11.glPushMatrix();
        if (tileEntity.entityItem != null) {
            renderItem.func_77014_a(tileEntity.entityItem, x + 0.5D, y + 0.4D, z + 0.5D, 0.0F, 0.0F);
        }
        GL11.glPopMatrix();
    }

    public void func_76894_a(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        render((TileQuantumAssembler)tileEntity, var2, var4, var6, var8);
    }
}
