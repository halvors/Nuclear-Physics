package org.halvors.nuclearphysics.client.render.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.ITileRotatable;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public abstract class RenderTile<T extends TileEntity> extends TileEntitySpecialRenderer {
    private final ResourceLocation texture;

    public RenderTile(String name) {
        this.texture = ResourceUtility.getResource(Resource.TEXTURE_MODELS, name + ".png");
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();

        bindTexture(texture);

        // Translate to the location of our tile entity
        GL11.glTranslated(x, y, z);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        // Rotate block based on direction.
        if (tile instanceof ITileRotatable) {
            RenderUtility.rotateBlockBasedOnDirection(((ITileRotatable) tile).getFacing());
        }

        render((T) tile, x, y, z);

        GL11.glPopMatrix();
    }

    protected abstract void render(T tile, double x, double y, double z);
}
