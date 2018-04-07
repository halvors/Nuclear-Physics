package org.halvors.nuclearphysics.common.item.tool;

import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.api.IMekWrench;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.item.IWrench;
import org.halvors.nuclearphysics.common.Integration;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@InterfaceList({
        @Interface(iface = "mekanism.api.IMekWrench", modid = Integration.MEKANISM_MOD_ID)
})
public class ItemWrench extends ItemBase implements IWrench, IMekWrench {
    public ItemWrench() {
        super("wrench");

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        list.add(LanguageUtility.transelate("tooltip.state") + ": " + getState(itemStack).getName());

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            WrenchState state = getState(itemStack);
            int toSet = state.ordinal() < WrenchState.values().length - 1 ? state.ordinal() + 1 : 0;
            setState(itemStack, WrenchState.values()[toSet]);
            state = getState(itemStack);

            if (!world.isRemote) {
                player.addChatMessage(new ChatComponentText(Color.DARK_BLUE + "[" + Reference.NAME + "] " + Color.GREY + LanguageUtility.transelate("tooltip.state") + ": " + state.getColor() + state.getName()));

                return itemStack;
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int facing, float hitX, float hitY, float hitZ) {
        final Block block = world.getBlock(x, y, z);

        switch (getState(itemStack)) {
            case ROTATE:
                ForgeDirection[] validRotations = block.getValidRotations(world, x, y, z);

                if (validRotations != null && validRotations.length > 0) {
                    block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(facing));

                    return true;
                }
        }

        return false;
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return getState(player.getHeldItem()) == WrenchState.WRENCH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canUseWrench(EntityPlayer player, int x, int y, int z) {
        return getState(player.getHeldItem()) == WrenchState.WRENCH;
    }

    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer player, int x, int y, int z) {
        return getState(player.getHeldItem()) == WrenchState.WRENCH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public WrenchState getState(ItemStack itemStack) {
        return WrenchState.values()[InventoryUtility.getNBTTagCompound(itemStack).getInteger("state")];
    }

    public void setState(ItemStack itemStack, WrenchState state) {
        InventoryUtility.getNBTTagCompound(itemStack).setInteger("state", state.ordinal());
    }

    public enum WrenchState {
        WRENCH("wrench", Color.BRIGHT_GREEN),
        ROTATE("rotate", Color.YELLOW);

        private String name;
        private Color color;

        WrenchState(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return LanguageUtility.transelate("tooltip." + name);
        }

        public Color getColor() {
            return color;
        }
    }
}