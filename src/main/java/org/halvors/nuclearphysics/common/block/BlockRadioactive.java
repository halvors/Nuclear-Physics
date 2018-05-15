package org.halvors.nuclearphysics.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;
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
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        if ((spawnParticle || General.allowRadioactiveOres) && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
            int radius = 3;

            for (int i = 0; i < 2; i++) {
                final BlockPos newPos = new BlockPos(x, y, z).add(random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2, random.nextDouble() * radius - radius / 2);
                final EntitySmokeFX fx = new EntitySmokeFX(world, newPos.getX(), newPos.getY(), newPos.getZ(), (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);
                fx.setRBGColorF(0.2F, 0.8F, 0);
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (!world.isRemote) {
            if (isRandomlyRadioactive || General.allowRadioactiveOres) {
                final AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                final List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

                for (EntityLivingBase entity : entitiesNearby) {
                    ModPotions.poisonRadiation.poisonEntity(entity, amplifier);
                }
            }

            if (canSpread) {
                for (int side = 0; side < 4; side++) {
                    int newX = x + random.nextInt(3) - 1;
                    int newY = y + random.nextInt(5) - 3;
                    int newZ = z + random.nextInt(3) - 1;
                    final Block block = world.getBlock(newX, newY, newZ);

                    if (random.nextFloat() > 0.4 && (block == Blocks.farmland || block == Blocks.grass)) {
                        world.setBlock(newX, newY, newZ, this);
                    }
                }

                if (random.nextFloat() > 0.85) {
                    world.setBlock(x, y, z, Blocks.dirt);
                }
            }
        }
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: worldgen, x, y, z, entity
     */
    @Override
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity instanceof EntityLivingBase && (canWalkPoison || General.allowRadioactiveOres)) {
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity);
        }
    }
}