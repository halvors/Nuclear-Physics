package org.halvors.nuclearphysics.common.block.reactor.fission;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.block.IRadioactiveBlock;
import org.halvors.nuclearphysics.client.render.particle.ParticleRadioactive;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockBase;
import org.halvors.nuclearphysics.common.block.states.BlockStateRadioactive;
import org.halvors.nuclearphysics.common.block.states.BlockStateRadioactive.EnumRadioactive;
import org.halvors.nuclearphysics.common.init.ModPotions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockRadioactive extends BlockBase implements IRadioactiveBlock {
    public BlockRadioactive() {
        super("radioactive", EnumRadioactive.DIRT.getMaterial());

        setTickRandomly(true);
        setHardness(0.2F);
        setLightLevel(0.1F);
        setDefaultState(blockState.getBaseState().withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.DIRT));
    }

    @Override
    public void registerItemModel(final ItemBlock itemBlock) {
        for (final EnumRadioactive type : EnumRadioactive.values()) {
            NuclearPhysics.getProxy().registerItemRenderer(itemBlock, type.ordinal(), name, "type=" + type.getName());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(@Nonnull final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
        for (final EnumRadioactive type : EnumRadioactive.values()) {
            list.add(new ItemStack(item, 1, type.ordinal()));
        }
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState() {
        return new BlockStateRadioactive(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(final int metadata) {
        return getDefaultState().withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.values()[metadata]);
    }

    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockStateRadioactive.TYPE).ordinal();
    }

    @Override
    public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase entity, final ItemStack itemStack) {
        world.setBlockState(pos, state.withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.values()[itemStack.getItemDamage()]), 2);
    }

    @Override
    public int damageDropped(final IBlockState state) {
        return getMetaFromState(state);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public Material getMaterial(final IBlockState state) {
        return state.getValue(BlockStateRadioactive.TYPE).getMaterial();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public SoundType getSoundType(final IBlockState state, final World world, final BlockPos pos, @Nullable final Entity entity) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        return type.getSoundType();
    }

    @Override
    public int getHarvestLevel(@Nonnull final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case URANIUM_ORE:
                return 2;
        }

        return super.getHarvestLevel(state);
    }

    @Override
    public String getHarvestTool(@Nonnull final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case URANIUM_ORE:
                return "pickaxe";
        }

        return super.getHarvestTool(state);
    }

    @Override
    public int quantityDropped(final IBlockState state, final int fortune, @Nonnull final Random random) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return 0;
        }

        return super.quantityDropped(state, fortune, random);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random random) {
        if (shouldSpawnParticles(state) && General.allowRadioactiveOres && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
            final int radius = getRadius(state);

            for (int i = 0; i < 2; i++) {
                BlockPos newPos = pos.add(random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2);
                RenderUtility.renderParticle(new ParticleRadioactive(world, newPos.getX(), newPos.getY(), newPos.getZ(), (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2));
            }
        }
    }

    @Override
    public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random random) {
        if (!world.isRemote) {
            final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);
            final boolean isBlockOver = !world.isAirBlock(pos.up());

            // Update block type based on neighbor blocks.
            switch (type) {
                case DIRT:
                    if (!isBlockOver) {
                        world.setBlockState(pos, getDefaultState().withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.GRASS));
                    }

                case GRASS:
                    if (isBlockOver) {
                        world.setBlockState(pos, getDefaultState().withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.DIRT));
                    }
            }

            if (canPoisonEntity(state) && General.allowRadioactiveOres) {
                final int radius = getRadius(state);
                final AxisAlignedBB bounds = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
                final List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

                for (EntityLivingBase entity : entitiesNearby) {
                    ModPotions.poisonRadiation.poisonEntity(entity, getAmplifier(state));
                }
            }

            if (canSpread(state)) {
                for (final EnumFacing side : EnumFacing.values()) {
                    final BlockPos newPos = pos.offset(side);
                    final IBlockState newState = world.getBlockState(newPos);

                    if (random.nextFloat() > 0.4 && newState == Blocks.GRASS.getDefaultState() || newState == Blocks.FARMLAND.getDefaultState()) {
                        final EnumRadioactive newType = world.isAirBlock(newPos.up()) ? EnumRadioactive.GRASS : EnumRadioactive.DIRT;

                        world.setBlockState(newPos, getDefaultState().withProperty(BlockStateRadioactive.TYPE, newType));
                    }
                }

                if (random.nextFloat() > 0.85) {
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                }
            }
        }
    }

    /**
     * Called whenever an entity is walking on top of this block.
     */
    @Override
    public void onEntityCollidedWithBlock(final World world, final BlockPos pos, final IBlockState state, final Entity entity) {
        if (entity instanceof EntityLivingBase && canPoisonEntity(state) && General.allowRadioactiveOres) {
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canSpread(final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return true;
        }

        return false;
    }

    @Override
    public int getRadius(final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return 5;
        }

        return 1;
    }

    @Override
    public int getAmplifier(final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return 2;
        }

        return 0;
    }

    @Override
    public boolean canPoisonEntity(final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return true;
        }

        return false;
    }

    @Override
    public boolean shouldSpawnParticles(final IBlockState state) {
        final EnumRadioactive type = state.getValue(BlockStateRadioactive.TYPE);

        switch (type) {
            case DIRT:
            case GRASS:
                return true;
        }

        return false;
    }
}
