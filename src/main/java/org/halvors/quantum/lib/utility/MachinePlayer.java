package org.halvors.quantum.lib.utility;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.VectorWorld;

import java.util.LinkedHashMap;

public class MachinePlayer extends FakePlayer {
    private static final LinkedHashMap<World, MachinePlayer> FAKE_PLAYERS = new LinkedHashMap<>();

    public MachinePlayer(World world, String name, String suffix) {
        super((WorldServer) world, new GameProfile(null, "FakePlayer"));
    }

    public MachinePlayer(World world, String suffix) {
        this(world, "FakePlayer", suffix);
    }

    public MachinePlayer(World world) {
        this(world, "(" + world.provider.dimensionId + ")");
    }

    public static MachinePlayer get(World world) {
        if (!FAKE_PLAYERS.containsKey(world) || FAKE_PLAYERS.get(world) == null) {
            FAKE_PLAYERS.put(world, new MachinePlayer(world));
        }

        return FAKE_PLAYERS.get(world);
    }

    public static boolean useItemAt(ItemStack itemStack, VectorWorld location, ForgeDirection direction) {
        return useItemAt(itemStack, location.world, location.intX(), location.intY(), location.intZ(), direction.ordinal(), 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side) {
        return useItemAt(itemStack, world, x, y, z, side, 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side, int hitX, int hitY, int hitZ) {
        if (itemStack != null && itemStack.getItem() != null) {
            return itemStack.getItem().onItemUse(itemStack, get(world), world, x, y, z, side, hitX, hitY, hitZ);
        }

        return false;
    }
}
