package org.halvors.quantum.client.render.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.client.render.ModelCube;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer {
    private static final IModelCustom modelTop = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellTop.obj"));
    private static final IModelCustom modelMiddle = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellMiddle.obj"));
    private static final IModelCustom modelBottom = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/reactorCellBottom.obj"));
    private static final ResourceLocation textureTop = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellTop.png");
    private static final ResourceLocation textureMiddle = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellMiddle.png");
    private static final ResourceLocation textureBottom = new ResourceLocation(Reference.PREFIX + "textures/models/reactorCellBottom.png");
    private static final ResourceLocation textureFissile = new ResourceLocation(Reference.PREFIX + "textures/models/reactorFissileMaterial.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            int metadata = 2;

            if (tileReactorCell.getWorld() != null) {
                metadata = tileEntity.getBlockMetadata();
            }

            boolean hasBelow = tileReactorCell.getWorld() != null && tileReactorCell.getWorld().getTileEntity(tileReactorCell.xCoord, tileReactorCell.yCoord - 1, tileReactorCell.zCoord) instanceof TileReactorCell;

            switch (metadata) {
                case 0:
                    RenderUtility.bind(textureBottom);
                    modelBottom.renderAll();
                    break;

                case 1:
                    RenderUtility.bind(textureMiddle);
                    modelMiddle.renderAll();
                    break;

                case 2:
                    RenderUtility.bind(textureTop);
                    GL11.glScaled(1, 1.3, 1);

                    if (hasBelow) {
                        GL11.glTranslated(0, -0.125, 0);
                        modelTop.renderAllExcept("BottomPad", "BaseDepth", "BaseWidth", "Base");
                    } else {
                        modelTop.renderAll();
                    }
                    break;
            }

            // Render fissile fuel inside reactor.
            ItemStack itemStackFuel = tileReactorCell.getStackInSlot(0);

            if (itemStackFuel != null) {
                float height = tileReactorCell.getHeight() * (((float) itemStackFuel.getMaxDurability() - itemStackFuel.getMetadata()) / (float) itemStackFuel.getMaxDurability());

                GL11.glPushMatrix();
                RenderUtility.bind(textureFissile);
                GL11.glScaled(0.4, 1.6 * height, 0.4);
                RenderUtility.disableLighting();
                ModelCube.instance.render();
                RenderUtility.enableLighting();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }
    }
}
