package org.halvors.nuclearphysics.common.item;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import mekanism.api.IMekWrench;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class ItemWrench extends ItemBase implements IToolWrench, IToolHammer, IMekWrench {
    public ItemWrench(String name) {
        super(name);

        setMaxStackSize(1);
    }

    // BuildCraft
    @Override
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack itemStack, RayTraceResult rayTrace) {
        return true;
    }

    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack itemStack, RayTraceResult rayTrace) {
        player.swingArm(hand);
    }

    // CoFH
    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase player, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isUsable(ItemStack itemStack, EntityLivingBase player, Entity entity) {
        return true;
    }

    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase player, BlockPos pos) {
        player.swingArm(player.getActiveHand());
    }

    @Override
    public void toolUsed(ItemStack itemStack, EntityLivingBase player, Entity entity) {
        player.swingArm(player.getActiveHand());
    }

    // Mekanism
    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer player, BlockPos pos) {
        return true;
    }
}
