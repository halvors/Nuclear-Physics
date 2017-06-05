package org.halvors.quantum.client.render.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.TileReactorCell;
import org.halvors.quantum.lib.render.block.ModelCube;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer {
    private static final IModelCustom MODEL_TOP = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellTop.obj"));
    private static final IModelCustom MODEL_MIDDLE = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellMiddle.obj"));
    private static final IModelCustom MODEL_BOTTOM = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellBottom.obj"));
    private static final ResourceLocation TEXTURE_TOP = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellTop.png");
    private static final ResourceLocation TEXTURE_MIDDLE = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellMiddle.png");
    private static final ResourceLocation TEXTURE_BOTTOM = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellBottom.png");
    private static final ResourceLocation TEXTURE_FISSILE = new ResourceLocation(Reference.PREFIX + "textures/models/fissileMaterial.png"); // TODO: Add this.

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileReactorCell tileReactorCell = (TileReactorCell) tileEntity;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);

        int meta = 2;

        if (tileEntity.getWorld() != null) {
            meta = tileEntity.getBlockMetadata();
        }

        boolean hasBelow = (tileEntity.getWorld() != null && tileEntity.getWorld().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) instanceof TileReactorCell);

        switch (meta) {
            case 0:
                bindTexture(TEXTURE_BOTTOM);
                MODEL_BOTTOM.renderAll();
                break;
            case 1:
                bindTexture(TEXTURE_MIDDLE);
                GL11.glTranslatef(0.0F, 0.075F, 0.0F);
                GL11.glScalef(1.0F, 1.15F, 1.0F);
                MODEL_MIDDLE.renderAll();
                break;
            case 2:
                bindTexture(TEXTURE_TOP);

                if (hasBelow) {
                    GL11.glScalef(1.0F, 1.32F, 1.0F);
                } else {
                    GL11.glTranslatef(0.0F, 0.1F, 0.0F);
                    GL11.glScalef(1.0F, 1.2F, 1.0F);
                }

                if (hasBelow) {
                    MODEL_TOP.renderAllExcept("BottomPad", "BaseDepth", "BaseWidth", "Base");
                } else {
                    MODEL_TOP.renderAll();
                }

                break;
        }

        GL11.glPopMatrix();

        if (tileReactorCell.getStackInSlot(0) != null) {
            float height = tileReactorCell.getHeight() * ((tileReactorCell.getStackInSlot(0).getMaxDurability() - tileReactorCell.getStackInSlot(0).getMetadata()) / tileReactorCell.getStackInSlot(0).getMaxDurability());

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F * height, (float) z + 0.5F);
            GL11.glScalef(0.4F, 0.9F * height, 0.4F);
            bindTexture(TEXTURE_FISSILE);
            RenderUtility.disableLighting();
            ModelCube.INSTNACE.render();
            RenderUtility.enableLighting();
            GL11.glPopMatrix();
        }
    }
}
