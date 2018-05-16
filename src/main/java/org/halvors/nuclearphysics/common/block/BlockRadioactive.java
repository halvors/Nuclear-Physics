package org.halvors.nuclearphysics.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.particle.ParticleRadioactive;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModPotions;

import java.util.List;
import java.util.Random;

public class BlockRadioactive extends BlockBase {
    protected boolean canSpread;
    protected float radius;
    protected int amplifier;
    protected boolean canWalkPoison;
    protected boolean isRandomlyRadioactive;
    protected boolean spawnParticle;

    public BlockRadioactive(final String name, final Material material) {
        super(name, material);

        setTickRandomly(true);
        setHardness(0.2F);
        setLightLevel(0.1F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random random) {
        if ((spawnParticle || General.allowRadioactiveOres) && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
            int radius = 3;

            for (int i = 0; i < 2; i++) {
                final BlockPos newPos = pos.add(random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2);
                RenderUtility.renderParticle(new ParticleRadioactive(world, newPos.getX(), newPos.getY(), newPos.getZ(), (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2));
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random random) {
        if (!world.isRemote) {
            if (isRandomlyRadioactive || General.allowRadioactiveOres) {
                final AxisAlignedBB bounds = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
                final List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

                for (EntityLivingBase entity : entitiesNearby) {
                    ModPotions.poisonRadiation.poisonEntity(entity, amplifier);
                }
            }

            if (canSpread) {
                for (int side = 0; side < 4; side++) {
                    final BlockPos newPos = new BlockPos(pos.getX() + random.nextInt(3) - 1, pos.getY() + random.nextInt(5) - 3, pos.getZ() + random.nextInt(3) - 1);
                    final Block block = world.getBlockState(newPos).getBlock();

                    if (random.nextFloat() > 0.4 && (block == Blocks.FARMLAND || block == Blocks.GRASS)) {
                        world.setBlockState(newPos, getDefaultState());
                    }
                }

                if (random.nextFloat() > 0.85) {
                    world.setBlockState(pos, Blocks.DIRT.getDefaultState());
                }
            }
        }
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: worldgen, x, y, z, entity
     */
    @Override
    public void onEntityWalk(final World world, final BlockPos pos, final Entity entity) {
        if (entity instanceof EntityLivingBase && (canWalkPoison || General.allowRadioactiveOres)) {
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity);
        }
    }
}