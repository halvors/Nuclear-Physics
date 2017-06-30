package org.halvors.quantum.common.effect.poison;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.halvors.quantum.api.block.IAntiPoisonBlock;
import org.halvors.quantum.api.item.armor.IAntiPoisonArmor;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.EnumSet;
import java.util.HashMap;

public abstract class Poison {
    public enum ArmorType {
        HELM,
        BODY,
        LEGGINGS,
        BOOTS
    }

    private final HashMap<String, Poison> poisons = new HashMap<>();
    protected String name;
    protected EnumSet<ArmorType> armorRequired = EnumSet.range(ArmorType.HELM, ArmorType.BOOTS);

    public Poison(String name) {
        this.name = name;
        this.poisons.put(name, this);
    }

    public String getName() {
        return name;
    }

    public EnumSet<ArmorType> getArmorRequired() {
        return armorRequired;
    }

    public void poisonEntity(Vector3 emitPosition, EntityLivingBase entity, int amplifier) {
        if (!isEntityProtected(emitPosition, entity, amplifier)) {
            doPoisonEntity(emitPosition, entity, amplifier);
        }
    }

    public void poisonEntity(Vector3 emitPosition, EntityLivingBase entity) {
        poisonEntity(emitPosition, entity, 0);
    }

    public boolean isEntityProtected(Vector3 emitPosition, EntityLivingBase entity, int amplifier) {
        EnumSet<ArmorType> armorWorn = EnumSet.noneOf(ArmorType.class);

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entity;

            for (int i = 0; i < entityPlayer.inventory.armorInventory.length; i++) {
                if (entityPlayer.inventory.armorInventory[i] != null) {
                    if ((entityPlayer.inventory.armorInventory[i].getItem() instanceof IAntiPoisonArmor)) {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor)entityPlayer.inventory.armorInventory[i].getItem();

                        if (armor.isProtectedFromPoison(entityPlayer.inventory.armorInventory[i], entity, getName())) {
                            armorWorn.add(ArmorType.values()[(armor.getArmorType().ordinal() % ArmorType.values().length)]);

                            armor.onProtectFromPoison(entityPlayer.inventory.armorInventory[i], entity, getName());
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

    protected abstract void doPoisonEntity(Vector3 paramVector3, EntityLivingBase paramEntityLivingBase, int paramInt);
}