package org.halvors.quantum.client.render.reactor.fission;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.render.ModelCube;
import org.halvors.quantum.client.render.OBJBakedModel;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell.EnumReactorCell;
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

    private ResourceLocation textureFissile = new ResourceLocation(Reference.ID, "models/reactor_fissile_material.png");

    @Override
    public void renderTileEntityAt(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        EnumReactorCell type = EnumReactorCell.values()[tile.getBlockMetadata()];
        boolean hasBelow = tile.getWorld().getTileEntity(tile.getPos().down()) instanceof TileReactorCell;

        switch (type) {
            case TOP:
                if (hasBelow) {
                    GlStateManager.translate(0, -0.125, 0);
                    modelTopBelow.render();
                } else {
                    modelTop.render();
                }
                break;

            case MIDDLE:
                modelMiddle.render();
                break;

            case BOTTOM:
                modelBottom.render();
                break;
        }

        // Render fissile fuel inside reactor.
        ItemStack itemStackFuel = tile.getStackInSlot(0);

        if (itemStackFuel != null) {
            float height = tile.getHeight() * (((float) itemStackFuel.getMaxDamage() - itemStackFuel.getMetadata()) / (float) itemStackFuel.getMaxDamage());

            bindTexture(textureFissile);

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5, 0, 0.5);
            GlStateManager.scale(0.4, 1.6 * height, 0.4);
            GlStateManager.disableLighting();
            ModelCube.getInstance().render();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}