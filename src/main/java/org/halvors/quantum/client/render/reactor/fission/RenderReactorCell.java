package org.halvors.quantum.client.render.reactor.fission;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.render.ModelCube;
import org.halvors.quantum.client.render.OBJBakedModel;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer<TileReactorCell> {
    private static final OBJBakedModel modelTopBelow = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_top.obj"), Arrays.asList("Base", "BaseDepth", "BaseWidth", "BottomPad"));
    private static final OBJBakedModel modelTop = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_top.obj"), Arrays.asList("BackLeftSpike", "BackRightSpike", "FrontLeftSpike", "FrontRightSpike", "HatCover", "HatDepth", "HatMiddle", "HatTop", "HatWidth", "MiddleBackLeft", "MiddleBackRight", "MiddleFrontLeft", "MiddleFrontRight", "MiddlePBack", "MiddlePFront", "MiddlePLeft", "MiddlePRight", "OPBackLeft", "OPBackRight", "OPFrontLeft", "OPFrontRight", "OPLeftBack", "OPLeftFront1", "OPLeftFront2", "OPRightBack", "TopBase", "TopBaseDepth", "TopBaseWidth"));
    private static final OBJBakedModel modelMiddle = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_middle.obj"));
    private static final OBJBakedModel modelBottom = new OBJBakedModel(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_bottom.obj"));

    private static final ResourceLocation textureFissile = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "blocks/reactor_fissile_material.png");

    @Override
    public void renderTileEntityAt(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x + 0.5, y, z + 0.5);
        GlStateManager.disableRescaleNormal();

        int metadata = 2;

        if (tile.getWorld() != null) {
            metadata = tile.getBlockMetadata();
        }

        boolean hasBelow = tile.getWorld().getTileEntity(tile.getPos().up()) instanceof TileReactorCell;

        switch (metadata) {
            case 0:
                modelBottom.render();
                break;

            case 1:
                modelMiddle.render();
                break;

            case 2:
                GlStateManager.scale(1, 1.3, 1);

                if (hasBelow) {
                    GlStateManager.translate(0, -0.125, 0);
                    modelTopBelow.render();
                } else {
                    modelTop.render();
                }
                break;
        }

        // Render fissile fuel inside reactor.
        ItemStack itemStackFuel = tile.getStackInSlot(0);

        if (itemStackFuel != null) {
            float height = tile.getHeight() * (((float) itemStackFuel.getMaxDamage() - itemStackFuel.getMetadata()) / (float) itemStackFuel.getMaxDamage());

            bindTexture(textureFissile);

            GlStateManager.pushMatrix();
            GlStateManager.scale(0.4, 1.6 * height, 0.4);
            GlStateManager.disableLighting();
            ModelCube.instance.render();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}