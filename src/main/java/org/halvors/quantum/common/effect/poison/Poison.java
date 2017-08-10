package org.halvors.quantum.common.effect.poison;

import javafx.geometry.Pos;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.api.block.IAntiPoisonBlock;
import org.halvors.quantum.api.item.armor.IAntiPoisonArmor;
import org.halvors.quantum.common.utility.position.Position;

import java.util.EnumSet;

public abstract class Poison {
    private static final EnumSet<EntityEquipmentSlot> armorRequired = EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);
    private String name;

    public Poison(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public EnumSet<EntityEquipmentSlot> getArmorRequired() {
        return armorRequired;
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier) {
        if (!isEntityProtected(pos, entity, amplifier)) {
            doPoisonEntity(pos, entity, amplifier);
        }
    }

    public void poisonEntity(BlockPos pos, EntityLivingBase entity) {
        poisonEntity(pos, entity, 0);
    }

    public boolean isEntityProtected(BlockPos pos, EntityLivingBase entity, int amplifier) {
        EnumSet<EntityEquipmentSlot> armorWorn = EnumSet.noneOf(EntityEquipmentSlot.class);

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;

            for (int i = 0; i < entityPlayer.inventory.armorInventory.length; i++) {
                ItemStack itemStack = entityPlayer.inventory.armorInventory[i];

                if (itemStack != null) {
                    if ((itemStack.getItem() instanceof IAntiPoisonArmor)) {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor) itemStack.getItem();

                        if (armor.isProtectedFromPoison(itemStack, entity, name)) {
                            armorWorn.add(EntityEquipmentSlot.values()[(armor.getArmorType().ordinal() % EntityEquipmentSlot.values().length)]);

                            armor.onProtectFromPoison(itemStack, entity, name);
                        }
                    }
                }
            }
        }

        return armorWorn.containsAll(armorRequired);
    }

    public int getAntiPoisonBlockCount(World world, Position startingPosition, Position endingPosition) {
        Position delta = endingPosition.subtract(startingPosition).normalize();
        //Position targetPosition = startingPosition.clone();
        double totalDistance = startingPosition.distance(endingPosition);

        int count = 0;
        if (totalDistance > 1.0D) {
            while (startingPosition.distance(endingPosition) <= totalDistance) {
                Block block = startingPosition.getBlock(world);
                if (block instanceof IAntiPoisonBlock) {
                    if (((IAntiPoisonBlock) block).isPoisonPrevention(world, startingPosition.getIntX(), startingPosition.getIntY(), startingPosition.getIntZ(), name)) {
                        count++;
                    }
                }

                startingPosition.add(delta);
            }
        }

        return count;
    }

    protected abstract void doPoisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier);
}