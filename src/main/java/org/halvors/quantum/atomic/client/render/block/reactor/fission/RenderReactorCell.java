package org.halvors.quantum.atomic.client.render.block.reactor.fission;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.render.block.ModelCube;
import org.halvors.quantum.atomic.client.render.block.OBJModelContainer;
import org.halvors.quantum.atomic.client.utility.RenderUtility;
import org.halvors.quantum.atomic.common.Reference;
import org.halvors.quantum.atomic.common.block.reactor.fission.BlockReactorCell.EnumReactorCell;
import org.halvors.quantum.atomic.common.block.states.BlockStateReactorCell;
import org.halvors.quantum.atomic.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.atomic.common.utility.ResourceUtility;
import org.halvors.quantum.atomic.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer<TileReactorCell> {
    private static final OBJModelContainer modelTopBelow = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_top.obj"), Arrays.asList("BackLeftSpike", "BackRightSpike", "FrontLeftSpike", "FrontRightSpike", "HatCover", "HatDepth", "HatMiddle", "HatTop", "HatWidth", "MiddleBackLeft", "MiddleBackRight", "MiddleFrontLeft", "MiddleFrontRight", "MiddlePBack", "MiddlePFront", "MiddlePLeft", "MiddlePRight", "OPBackLeft", "OPBackRight", "OPFrontLeft", "OPFrontRight", "OPLeftBack", "OPLeftFront1", "OPLeftFront2", "OPRightBack", "TopBase", "TopBaseDepth", "TopBaseWidth"));
    private static final OBJModelContainer modelTop = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_top.obj"), Arrays.asList("BackLeftSpike", "BackRightSpike", "Base", "BaseDepth", "BaseWidth", "BottomPad", "FrontLeftSpike", "FrontRightSpike", "HatCover", "HatDepth", "HatMiddle", "HatTop", "HatWidth", "MiddleBackLeft", "MiddleBackRight", "MiddleFrontLeft", "MiddleFrontRight", "MiddlePBack", "MiddlePFront", "MiddlePLeft", "MiddlePRight", "OPBackLeft", "OPBackRight", "OPFrontLeft", "OPFrontRight", "OPLeftBack", "OPLeftFront1", "OPLeftFront2", "OPRightBack", "TopBase", "TopBaseDepth", "TopBaseWidth"));
    private static final OBJModelContainer modelMiddle = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_middle.obj"));
    private static final OBJModelContainer modelBottom = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_bottom.obj"));
    private static final ResourceLocation textureFissileMaterial = new ResourceLocation(Reference.ID, "textures/models/reactor_fissile_material.png");

    @Override
    public void renderTileEntityAt(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        EnumReactorCell type = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockStateReactorCell.TYPE);
        boolean hasBelow = tile.getWorld().getTileEntity(tile.getPos().down()) instanceof TileReactorCell;

        switch (type) {
            case TOP:
                if (hasBelow) {
                    GlStateManager.translate(0, -0.163, 0);
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
        if (tile.getMultiBlock().isPrimary()) {
            ItemStack itemStackFuel = tile.getMultiBlock().get().getInventory().getStackInSlot(0);

            if (itemStackFuel != null) {
                float height = tile.getHeight() * (((float) itemStackFuel.getMaxDamage() - itemStackFuel.getMetadata()) / (float) itemStackFuel.getMaxDamage());

                bindTexture(textureFissileMaterial);

                GlStateManager.pushMatrix();
                GlStateManager.translate(0.5, 0, 0.5);
                GlStateManager.scale(0.4, 1.6 * height, 0.4);
                GlStateManager.disableLighting();
                ModelCube.getInstance().render();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();
    }
}