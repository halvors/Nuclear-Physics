package org.halvors.quantum.common.block.reactor.fusion;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.block.BlockContainerQuantum;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.List;

public class BlockElectromagnet extends BlockContainerQuantum { //implements IBlockCustomRender {
    //private static IIcon iconTop, iconGlass;

    public BlockElectromagnet() {
        super("electromagnet", Material.IRON);

        setResistance(20);
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconTop = iconRegister.registerIcon(Reference.PREFIX + "electromagnet_top");
        iconGlass = iconRegister.registerIcon(Reference.PREFIX + "electromagnetGlass");
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
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 0;
    }
    */

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        Vector3 neighborPosition = new Vector3(pos.getX(), pos.getY(), pos.getZ()).translate(side.getOpposite());
        Block block = state.getBlock();
        int metadata = state.getBlock().getMetaFromState(state);
        Block neighborBlock = neighborPosition.getBlock(world);
        int neighborMetadata = neighborPosition.getBlock(world).getMetaFromState(state);

        // Transparent electromagnetic glass.
        if (block == this && neighborBlock == this && metadata == 1 && neighborMetadata == 1) {
            return false;
        }

        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubBlocks(item, creativeTabs, list);

        list.add(new ItemStack(item, 1, 1));
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public ISimpleBlockRenderer getRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
    */

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileElectromagnet();
    }
}