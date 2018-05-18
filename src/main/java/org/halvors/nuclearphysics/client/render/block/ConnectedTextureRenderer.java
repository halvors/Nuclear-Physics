package org.halvors.nuclearphysics.client.render.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.utility.BlockRenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ConnectedTextureRenderer implements ISimpleBlockRenderer {
    private Block block;
    private IIcon edgeTexture;

    public ConnectedTextureRenderer(Block block, String edgeTexture) {
        this.block = block;
        this.edgeTexture = TextureEventHandler.getIcon(edgeTexture + "_edge");
    }

    @Override
    public boolean renderItem(ItemStack itemStack) {
        GL11.glPushMatrix();

        BlockRenderUtility.tessellateBlockWithConnectedTextures(itemStack.getMetadata(), block, null, edgeTexture);

        GL11.glPopMatrix();

        return true;
    }

    @Override
    public boolean renderStatic(RenderBlocks renderer, IBlockAccess world, int x, int y, int z) {
        final BlockPos pos = new BlockPos(x, y, z);
        final TileEntity tile = pos.getTileEntity(world);
        byte sideMap = 0;

        for (ForgeDirection facing : ForgeDirection.VALID_DIRECTIONS) {
            final BlockPos checkPos = pos.offset(facing);
            final TileEntity checkTile = checkPos.getTileEntity(world);

            if (checkTile != null && checkTile.getClass() == tile.getClass() && checkPos.getBlockMetadata(world) == tile.getBlockMetadata()) {
                sideMap = BlockRenderUtility.setEnableSide(sideMap, facing, true);
            }
        }

        BlockRenderUtility.tessellateBlockWithConnectedTextures(sideMap, world, x, y, z, block, null, edgeTexture);

        return true;
    }
}