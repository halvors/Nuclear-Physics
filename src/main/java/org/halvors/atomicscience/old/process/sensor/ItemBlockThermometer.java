package org.halvors.atomicscience.old.process.sensor;

import calclavia.lib.prefab.item.ItemBlockSaved;
import calclavia.lib.utility.LanguageUtility;
import calclavia.lib.utility.nbt.NBTUtility;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;

public class ItemBlockThermometer
        extends ItemBlockSaved
{
    public static final int ENERGY_CONSUMPTION = 1000;

    public ItemBlockThermometer(int id)
    {
        super(id);
    }

    public void func_77624_a(ItemStack itemStack, EntityPlayer player, List par3List, boolean par4)
    {
        super.func_77624_a(itemStack, player, par3List, par4);
        Vector3 coord = getSavedCoord(itemStack);
        if (coord != null)
        {
            par3List.add("?" + LanguageUtility.getLocal("tooltip.trackingTemperature"));
            par3List.add("X: " + coord.intX() + ", Y: " + coord.intY() + ", Z: " + coord.intZ());
        }
        else
        {
            par3List.add("�4" + LanguageUtility.getLocal("tooltip.notTrackingTemperature"));
        }
    }

    public void setSavedCoords(ItemStack itemStack, Vector3 position)
    {
        NBTTagCompound nbt = NBTUtility.getNBTTagCompound(itemStack);
        if (position != null) {
            nbt.func_74766_a("trackCoordinate", position.writeToNBT(new NBTTagCompound()));
        } else {
            nbt.func_82580_o("trackCoordinate");
        }
    }

    public Vector3 getSavedCoord(ItemStack itemStack)
    {
        NBTTagCompound nbt = NBTUtility.getNBTTagCompound(itemStack);
        if (nbt.func_74764_b("trackCoordinate")) {
            return new Vector3(nbt.func_74775_l("trackCoordinate"));
        }
        return null;
    }

    public ItemStack func_77659_a(ItemStack itemStack, World world, EntityPlayer player)
    {
        setSavedCoords(itemStack, null);
        if (!world.field_72995_K) {
            player.func_71035_c("Cleared tracking coordinate.");
        }
        return itemStack;
    }

    public boolean func_77648_a(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (player.func_70093_af())
        {
            if (!world.field_72995_K)
            {
                setSavedCoords(itemStack, new Vector3(x, y, z));
                player.func_71035_c("Tracking coordinate: " + x + ", " + y + ", " + z);
            }
            return true;
        }
        return super.func_77648_a(itemStack, player, world, x, y, z, par7, par8, par9, par10);
    }
}
