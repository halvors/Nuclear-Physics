package org.halvors.quantum.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.List;
import java.util.Random;

public class BlockRadioactive extends BlockQuantum {
    protected boolean canSpread;
    protected float radius;
    protected int amplifier;
    protected boolean canWalkPoison;
    protected boolean isRandomlyRadioactive;
    protected boolean spawnParticle;

    public BlockRadioactive(String name, Material material) {
        super(name, material);

        setTickRandomly(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        if (spawnParticle && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
            int radius = 3;

            for (int i = 0; i < 2; i++) {
                //BlockPos newPos = pos.add(random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2);
                //world.spawnParticle(EnumParticleTypes.BLOCK_DUST, newPos.getX(), newPos.getY(), newPos.getZ(), (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);

                /*
                Vector3 position = new Vector3(x, y, z);
                position.add(random.nextDouble() * radius - radius / 2);

                EntitySmokeFX fx = new EntitySmokeFX(world, newPos.getX(), newPos.getY(), newPos.getZ(), (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);
                fx.setRBGColorF(0.2F, 0.8F, 0);
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
                */
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!world.isRemote) {
            if (isRandomlyRadioactive) {
                AxisAlignedBB bounds = new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
                List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

                for (EntityLivingBase entity : entitiesNearby) {
                    PoisonRadiation.getInstance().poisonEntity(pos, entity, amplifier);
                }
            }

            if (canSpread) {
                for (int side = 0; side < 4; side++) {
                    BlockPos newPos = new BlockPos(pos.getX() + random.nextInt(3) - 1, pos.getY() + random.nextInt(5) - 3, pos.getZ() + random.nextInt(3) - 1);
                    Block block = world.getBlockState(newPos).getBlock();

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
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityLiving && canWalkPoison) {
            PoisonRadiation.getInstance().poisonEntity(pos, (EntityLiving) entity);
        }
    }
}