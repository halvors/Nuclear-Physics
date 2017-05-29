package com.calclavia.core.lib.prefab.poison;

import com.calclavia.core.lib.Calclavia;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.EnumSet;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.calclavia.core.api.atomicscience.armor.IAntiPoisonArmor;
import com.calclavia.core.api.atomicscience.block.IAntiPoisonBlock;
import com.calclavia.core.lib.transform.vector.Vector3;

public abstract class Poison
{
    static HashMap<String, Poison> poisons = new HashMap();
    static BiMap<String, Integer> poisonIDs = HashBiMap.create();
    private static int maxID = 0;
    protected final boolean isDisabled;
    protected String name;
    protected EnumSet<ArmorType> armorRequired = EnumSet.range(ArmorType.HELM, ArmorType.BOOTS);

    public Poison(String name)
    {
        this.name = name;
        poisons.put(name, this);
        poisonIDs.put(name, Integer.valueOf(++maxID));
        this.isDisabled = Calclavia.CONFIGURATION.get("Disable Poison", "Disable " + this.name, false).getBoolean(false);
    }

    public static Poison getPoison(String name)
    {
        return (Poison)poisons.get(name);
    }

    public static Poison getPoison(int id)
    {
        return (Poison)poisons.get(getName(id));
    }

    public static String getName(int fluidID)
    {
        return (String)poisonIDs.inverse().get(Integer.valueOf(fluidID));
    }

    public static int getID(String name)
    {
        return ((Integer)poisonIDs.get(name)).intValue();
    }

    public String getName()
    {
        return this.name;
    }

    public final int getID()
    {
        return getID(getName());
    }

    public EnumSet<ArmorType> getArmorRequired()
    {
        return this.armorRequired;
    }

    public void poisonEntity(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
    {
        if (!isEntityProtected(emitPosition, entity, amplifier)) {
            doPoisonEntity(emitPosition, entity, amplifier);
        }
    }

    public void poisonEntity(Vector3 emitPosition, EntityLivingBase entity)
    {
        poisonEntity(emitPosition, entity, 0);
    }

    public boolean isEntityProtected(Vector3 emitPosition, EntityLivingBase entity, int amplifier)
    {
        EnumSet<ArmorType> armorWorn = EnumSet.noneOf(ArmorType.class);
        if ((entity instanceof EntityPlayer))
        {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            for (int i = 0; i < entityPlayer.inventory.armorInventory.length; i++) {
                if (entityPlayer.inventory.armorInventory[i] != null) {
                    if ((entityPlayer.inventory.armorInventory[i].getItem() instanceof IAntiPoisonArmor))
                    {
                        IAntiPoisonArmor armor = (IAntiPoisonArmor)entityPlayer.inventory.armorInventory[i].getItem();
                        if (armor.isProtectedFromPoison(entityPlayer.inventory.armorInventory[i], entity, getName()))
                        {
                            armorWorn.add(ArmorType.values()[(armor.getArmorType() % ArmorType.values().length)]);

                            armor.onProtectFromPoison(entityPlayer.inventory.armorInventory[i], entity, getName());
                        }
                    }
                }
            }
        }
        return armorWorn.containsAll(this.armorRequired);
    }

    public int getAntiPoisonBlockCount(World world, Vector3 startingPosition, Vector3 endingPosition)
    {
        Vector3 delta = (Vector3)((Vector3)endingPosition.clone().subtract(startingPosition)).normalize();
        Vector3 targetPosition = startingPosition.clone();
        double totalDistance = startingPosition.distance(endingPosition);

        int count = 0;
        if (totalDistance > 1.0D) {
            while (targetPosition.distance(endingPosition) <= totalDistance)
            {
                Block block = targetPosition.getBlock(world);
                if ((block instanceof IAntiPoisonBlock)) {
                    if (((IAntiPoisonBlock)block).isPoisonPrevention(world, targetPosition.intX(), targetPosition.intY(), targetPosition.intZ(), getName())) {
                        count++;
                    }
                }
                targetPosition.add(delta);
            }
        }
        return count;
    }

    protected abstract void doPoisonEntity(Vector3 paramVector3, EntityLivingBase paramEntityLivingBase, int paramInt);

    public static enum ArmorType
    {
        HELM,  BODY,  LEGGINGS,  BOOTS;

        private ArmorType() {}
    }
}
