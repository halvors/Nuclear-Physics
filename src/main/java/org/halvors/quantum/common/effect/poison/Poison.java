package org.halvors.quantum.common.effect.poison;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.quantum.api.block.IAntiPoisonBlock;
import org.halvors.quantum.api.item.armor.IAntiPoisonArmor;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.EnumSet;
import java.util.HashMap;

public abstract class Poison {
    private final HashMap<String, Poison> poisons = new HashMap<>();

    protected String name;
    protected EnumSet<EntityEquipmentSlot> armorRequired = EnumSet.of(EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET);

    public Poison(String name) {
        this.name = name;
        this.poisons.put(name, this);
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

            for (int i = 0; i < entityPlayer.inventory.armorInventory.size(); i++) {
                ItemStack itemStack = entityPlayer.inventory.armorInventory.get(i);

                if (!itemStack.isEmpty()) {
                    if ((itemStack.getItem() instanceof IAntiPoisonArmor)) {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor) itemStack.getItem();

                        if (armor.isProtectedFromPoison(itemStack, entity, getName())) {
                            armorWorn.add(EntityEquipmentSlot.values()[(armor.getArmorType().ordinal() % EntityEquipmentSlot.values().length)]);

                            armor.onProtectFromPoison(itemStack, entity, getName());
                        }
                    }
                }
            }
        }

        return armorWorn.containsAll(armorRequired);
    }

    public int getAntiPoisonBlockCount(World world, Vector3 startingPosition, Vector3 endingPosition) {
        Vector3 delta = endingPosition.clone().subtract(startingPosition).normalize();
        Vector3 targetPosition = startingPosition.clone();
        double totalDistance = startingPosition.distance(endingPosition);

        int count = 0;
        if (totalDistance > 1.0D) {
            while (targetPosition.distance(endingPosition) <= totalDistance) {
                Block block = targetPosition.getBlock(world);
                if (block instanceof IAntiPoisonBlock) {
                    if (((IAntiPoisonBlock) block).isPoisonPrevention(world, targetPosition.intX(), targetPosition.intY(), targetPosition.intZ(), getName())) {
                        count++;
                    }
                }

                targetPosition.add(delta);
            }
        }

        return count;
    }

    protected abstract void doPoisonEntity(BlockPos pos, EntityLivingBase entity, int amplifier);
}