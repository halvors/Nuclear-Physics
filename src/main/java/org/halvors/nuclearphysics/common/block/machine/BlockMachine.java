package org.halvors.nuclearphysics.common.block.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockInventory;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.tile.TileMachine;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockMachine extends BlockInventory {
    public BlockMachine() {
        super("machine", Material.IRON);

        setHardness(3.5F);
        setResistance(16);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateMachine.TYPE, EnumMachine.CHEMICAL_EXTRACTOR));
    }

    @Override
    public void registerItemModel(final ItemBlock itemBlock) {
        for (final EnumMachine type : EnumMachine.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(itemBlock, type.ordinal(), name, "facing=north,type=" + type.getName());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return state.getValue(BlockStateMachine.TYPE).getRenderType();
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFullCube(final IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final @Nonnull Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (final EnumMachine type : EnumMachine.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random random) {
        final EnumMachine type = state.getValue(BlockStateMachine.TYPE);
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMachine) {
            final TileMachine tileMachine = (TileMachine) tile;

            EnumParticleTypes particleTypes = null;
            final float xRandom = (float)pos.getX() + 0.5F;
            final float yRandom = (float)pos.getY() + 0.2F + random.nextFloat() * 6.0F / 16.0F;
            final float zRandom = (float)pos.getZ() + 0.5F;
            final float iRandom = 0.52F;
            final float jRandom = random.nextFloat() * 0.6F - 0.3F;
            final double xSpeed = 0;
            double ySpeed = 0;
            final double zSpeed = 0;

            switch (type) {
                case NUCLEAR_BOILER:
                    if (tileMachine.getOperatingTicks() > 0) {
                        particleTypes = EnumParticleTypes.CLOUD;
                        ySpeed = 0.05;
                    }
                    break;
            }

            if (particleTypes != null) {
                switch (tileMachine.getFacing()) {
                    case NORTH:
                        world.spawnParticle(particleTypes, (xRandom + jRandom), yRandom, (zRandom - iRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case SOUTH:
                        world.spawnParticle(particleTypes, (xRandom + jRandom), yRandom, (zRandom + iRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case WEST:
                        world.spawnParticle(particleTypes, (xRandom - iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        break;

                    case EAST:
                        world.spawnParticle(particleTypes, (xRandom + iRandom), yRandom, (zRandom + jRandom), xSpeed, ySpeed, zSpeed);
                        break;
                }
            }
        }
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateMachine(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(final int metadata) {
        return getDefaultState().withProperty(BlockStateMachine.TYPE, EnumMachine.values()[metadata]);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStateMachine.TYPE).ordinal();
    }

    @Override
    public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase entity, final ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateMachine.TYPE, EnumMachine.values()[itemStack.getItemDamage()]));

        super.onBlockPlacedBy(world, pos, state, entity, itemStack);
    }

    @Override
    public int damageDropped(final IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final ItemStack itemStack, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TilePlasmaHeater) {
            return FluidUtility.playerActivatedFluidItem(world, pos, player, itemStack, side);
        } else if (!player.isSneaking()) {
            PlayerUtility.openGui(player, world, pos);

            return true;
        }

        return super.onBlockActivated(world, pos, state, player, hand, itemStack, side, hitX, hitY, hitZ);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block) {
        final TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileMachine) {
            ((TileMachine) tile).updatePower();
        }
    }

    @Override
    public TileEntity createTileEntity(final @Nonnull World world, final @Nonnull IBlockState state) {
        final EnumMachine type = state.getValue(BlockStateMachine.TYPE);

        return type.getTileAsInstance();
    }
}