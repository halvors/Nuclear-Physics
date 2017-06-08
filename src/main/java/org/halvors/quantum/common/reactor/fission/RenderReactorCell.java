package org.halvors.quantum.common.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.lib.render.ModelCube;
import org.halvors.quantum.lib.utility.RenderUtility;
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
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y, z + 0.5D);

            int metadata = 2;

            if (tileReactorCell.getWorld() != null) {
                metadata = tileEntity.getBlockMetadata();
            }

            boolean hasBelow = (tileReactorCell.getWorld() != null && tileReactorCell.getWorld().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord) instanceof TileReactorCell);

            switch (metadata) {
                case 0:
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureBottom);

                    GL11.glTranslatef(0.0F, 0.035F, 0.0F); // Verified
                    GL11.glScalef(1.0F, 1F, 1.0F); // Verified

                    modelBottom.renderAll();
                    break;

                case 1:
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureMiddle);

                    GL11.glTranslatef(0.0F, 0.036F, 0.0F); // Verified
                    GL11.glScalef(1.0F, 1.0F, 1.0F); // Verified
                    modelMiddle.renderAll();
                    break;

                case 2:
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureTop);

                    if (hasBelow) {
                        GL11.glTranslatef(0.0F, -0.9F, 0.0F); // Verified
                        GL11.glScalef(1.0F, 1.42F, 1.0F); // Verified
                    } else {
                        GL11.glTranslatef(0.0F, 0.04F, 0.0F); // Verified
                        GL11.glScalef(1.0F, 1.2F, 1.0F); // Verified
                    }

                    if (hasBelow) {
                        modelTop.renderAllExcept("BottomPad", "BaseDepth", "BaseWidth", "Base");
                    } else {
                        modelTop.renderAll();
                    }
                    break;
            }

            GL11.glPopMatrix();

            // Render fissile fuel inside reactor.
            ItemStack itemStackFuel = tileReactorCell.getStackInSlot(0);

            if (itemStackFuel != null) {
                float height = tileReactorCell.getHeight() * ((itemStackFuel.getMaxDurability() - itemStackFuel.getMetadata()) / itemStackFuel.getMaxDurability());

                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F * height, (float) z + 0.5F);
                GL11.glScalef(0.4F, 0.9F * height, 0.4F);
                Minecraft.getMinecraft().renderEngine.bindTexture(textureFissile);
                RenderUtility.disableLighting();
                ModelCube.INSTNACE.render();
                RenderUtility.enableLighting();
                GL11.glPopMatrix();
            }
        }
    }
}
