package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;
import org.halvors.quantum.lib.render.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCentrifuge extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/centrifuge.obj"));
    private static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/centrifuge.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileCentrifuge) {
            TileCentrifuge tileCentrifuge = (TileCentrifuge) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);

            if (tileCentrifuge.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileCentrifuge.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glRotated(Math.toDegrees(tileCentrifuge.rotation), 0, 1, 0);
            model.renderOnly("C", "JROT", "KROT", "LROT", "MROT");
            GL11.glPopMatrix();

            model.renderAllExcept("C", "JROT", "KROT", "LROT", "MROT");
            GL11.glPopMatrix();
        }
    }
}