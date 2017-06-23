package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.List;
import java.util.Random;

public class BlockRadioactive extends BlockQuantum {
    protected static boolean canSpread;
    protected static float radius;
    protected static int amplifier;
    protected static boolean canWalkPoison;
    protected static boolean isRandomlyRadioactive;
    protected static boolean spawnParticle;

    public BlockRadioactive(String name) {
        super(name, Material.rock);

        setTickRandomly(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (spawnParticle) {
            if (Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
                int radius = 3;

                for (int i = 0; i < 2; i++) {
                    Vector3 pos = new Vector3(x, y, z);
                    pos.add(random.nextDouble() * radius - radius / 2);

                    EntitySmokeFX fx = new EntitySmokeFX(world, pos.x, pos.y, pos.z, (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D, (random.nextDouble() - 0.5D) / 2.0D);
                    fx.setRBGColorF(0.2F, 0.8F, 0);
                    Minecraft.getMinecraft().effectRenderer.addEffect(fx);
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            if (isRandomlyRadioactive) {
                AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                List<EntityLivingBase> entitiesNearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);

                for (EntityLivingBase entity : entitiesNearby) {
                    PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), entity, amplifier);
                }
            }

            if (canSpread) {
                for (int i = 0; i < 4; i++) {
                    int newX = x + random.nextInt(3) - 1;
                    int newY = y + random.nextInt(5) - 3;
                    int newZ = z + random.nextInt(3) - 1;
                    net.minecraft.block.Block block = world.getBlock(newX, newY, newZ);

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
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityLiving && canWalkPoison) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), (EntityLiving) entity);
        }
    }
}