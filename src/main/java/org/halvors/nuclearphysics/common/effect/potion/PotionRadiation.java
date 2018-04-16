package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModPotions;

import javax.annotation.Nonnull;

public class PotionRadiation extends PotionBase {
    private static final String name = "radiation";
    private static final DamageSource damageSource = new DamageSource(name).setDamageBypassesArmor();

    public PotionRadiation() {
        super(true, 78, 147, 49, name);

        setIconIndex(6, 0);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (entity.getEntityWorld().rand.nextFloat() > 0.9 - amplifier * 0.07) {
            entity.attackEntityFrom(damageSource, 1);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).addExhaustion(0.01F * (amplifier + 1));
            }
        }
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    protected void doEntityPoisoning(BlockPos pos, EntityLivingBase entity, int amplifier) {
        final World world = entity.getEntityWorld();
        final TileEntity tile = entity.getEntityWorld().getTileEntity(pos);

        // Do raytracing here.
        RayTraceResult rayTraceResult = world.rayTraceBlocks(new Vec3d(pos), new Vec3d(entity.getPosition()));

        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos resultPos = rayTraceResult.getBlockPos();

            NuclearPhysics.getLogger().info("RayTraceResult was successful.");
        }

        /*
        if (General.enableRadiationRoisoning) {
            entity.addPotionEffect(new PotionEffect(ModPotions.potionRadiation, 300 * (amplifier + 1), amplifier));
        }
        */
    }

    public static DamageSource getDamageSource() {
        return damageSource;
    }
}
