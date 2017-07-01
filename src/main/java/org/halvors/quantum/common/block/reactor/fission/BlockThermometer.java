package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.item.block.ItemBlockSaved;
import org.halvors.quantum.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.WrenchUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.ArrayList;
import java.util.List;

public class BlockThermometer extends BlockRotatable {
    //private static IIcon iconSide;

    public BlockThermometer() {
        super("thermometer", Material.PISTON);

        //setTextureName(Reference.PREFIX + "thermometer");
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 || side == 0 ? super.getIcon(side, meta) : iconSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconSide = iconRegister.registerIcon(Reference.PREFIX + "machine");
    }
    */

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack itemStack, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            if (WrenchUtility.hasUsableWrench(player, pos)) {
                if (player.isSneaking()) {
                    tileThermometer.setThreshold(tileThermometer.getThershold() - 10);
                } else {
                    tileThermometer.setThreshold(tileThermometer.getThershold() + 10);
                }

                return true;
            }

            if (player.isSneaking()) {
                tileThermometer.setThreshold(tileThermometer.getThershold() + 100);
            } else {
                tileThermometer.setThreshold(tileThermometer.getThershold() - 100);
            }

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        TileEntity tile = world.getTileEntity(pos);

        // Fetch saved coordinates from ItemBlockThermometer and apply them to the block.
        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;
            ItemBlockThermometer itemBlockThermometer = (ItemBlockThermometer) itemStack.getItem();
            tileThermometer.setTrackCoordinate(itemBlockThermometer.getSavedCoord(itemStack));
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            ItemStack itemStack = ItemBlockSaved.getItemStackWithNBT(state, world, pos);
            InventoryUtility.dropItemStack(world, new Vector3(pos.getX(), pos.getY(), pos.getZ()), itemStack);
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    // TODO: Port this.
    /*
    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side) {
        TileEntity tile = access.getTileEntity(x, y, z);

        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return 0;
    }
    */

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileThermometer();
    }
}
