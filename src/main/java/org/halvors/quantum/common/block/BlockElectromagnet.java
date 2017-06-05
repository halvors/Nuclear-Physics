package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.List;

/**
 * Electromagnet block
 */
public class BlockElectromagnet extends net.minecraft.block.Block implements IElectromagnet {
    private IIcon iconTop;
    private IIcon iconGlass;

    public BlockElectromagnet() {
        super(Material.iron);

        setUnlocalizedName("electromagnet");
        setTextureName(Reference.PREFIX + "electromagnet");
        setResistance(20F);

        //isOpaqueCube();
        //renderAsNormalBlock();

        //renderAsNormalBlock();
        //isOpaqueCube = false;
        //forceItemToRenderAsBlock = true
        //normalRender = false
        //isOpaqueCube = false
        //renderStaticBlock = true
        //this.itemBlock(classOf[ItemBlockMetadata])
    }

    /*
    blockResistance = 20
    forceItemToRenderAsBlock = true
    normalRender = false
    isOpaqueCube = false
    renderStaticBlock = true
    */

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata) {
        if (metadata == 1) {
            return iconGlass;
        }

        if (side == 0 || side == 1) {
            return iconTop;
        }

        return super.getIcon(side, metadata);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + getUnlocalizedName().replace("tile.", "") + "_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + getUnlocalizedName().replace("tile.", "") + "Glass");
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        if (access.getBlockMetadata(x, y, z) != 1) {
            return super.shouldSideBeRendered(access, x, y, z, side);
        }

        return true;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubBlocks(item, creativeTabs, list);

        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}

    /*
    @SideOnly(Side.CLIENT)
    public void renderInventory(ItemStack itemStack) {
        if (itemStack != null) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.5, 0.5, 0.5);
            RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getMetadata(), block, null, RenderUtility.getIcon(edgeTexture));
            GL11.glPopMatrix();
        } else {
            super.renderInventory(itemStack);
        }
    }
    */

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vector3 pos, float frame, int pass) {
        int sideMap = 0;

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
            Block block =

            int check = position + dir;
            val checkTile = check.getTileEntity(world)

            if (checkTile != null && checkTile.getClass == tile.getClass && check.getBlockMetadata(world) == tile.getBlockMetadata) {
                sideMap = WorldUtility.setEnableSide(sideMap, dir, true);
            }
        }

        RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, x, y, z, tile.getBlockType, null, RenderUtility.getIcon(edgeTexture))
    }
    */
