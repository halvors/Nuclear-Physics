package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCentrifuge extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/centrifuge.obj"));
    private static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/centrifuge.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        if (tile instanceof TileGasCentrifuge) {
            TileGasCentrifuge tileGasCentrifuge = (TileGasCentrifuge) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);

            if (tileGasCentrifuge.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileGasCentrifuge.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glRotated(Math.toDegrees(tileGasCentrifuge.rotation), 0, 1, 0);
            model.renderOnly("C", "JROT", "KROT", "LROT", "MROT");
            GL11.glPopMatrix();

            model.renderAllExcept("C", "JROT", "KROT", "LROT", "MROT");
            GL11.glPopMatrix();
        }
    }
}