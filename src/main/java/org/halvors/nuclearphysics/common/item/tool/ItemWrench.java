package org.halvors.nuclearphysics.common.item.tool;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mekanism.api.IMekWrench;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.item.IWrench;
import org.halvors.nuclearphysics.common.Integration;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.Arrays;
import java.util.List;

@InterfaceList({
        @Interface(iface = "buildcraft.api.tools.IToolWrench", modid = Integration.BUILDCRAFT_CORE_ID),
        @Interface(iface = "cofh.api.item.IToolHammer", modid = Integration.COFH_CORE_ID),
        @Interface(iface = "mekanism.api.IMekWrench", modid = Integration.MEKANISM_ID)
})
public class ItemWrench extends ItemBase implements IWrench, IToolWrench, IToolHammer, IMekWrench {
    public ItemWrench() {
        super("wrench");

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List list, final boolean flag) {
        final EnumWrenchState state = getState(itemStack);

        list.add(LanguageUtility.transelate("tooltip.state") + ": " + state.getColor() + state.getName());

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
        if (player.isSneaking()) {
            EnumWrenchState state = getState(itemStack);
            final int toSet = state.ordinal() < EnumWrenchState.values().length - 1 ? state.ordinal() + 1 : 0;
            setState(itemStack, EnumWrenchState.values()[toSet]);
            state = getState(itemStack);

            if (!world.isRemote) {
                player.addChatMessage(new ChatComponentText(EnumColor.DARK_BLUE + "[" + Reference.NAME + "] " + EnumColor.GREY + LanguageUtility.transelate("tooltip.state") + ": " + state.getColor() + state.getName()));

                return itemStack;
            }
        }

        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUse(final ItemStack itemStack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        final BlockPos pos = new BlockPos(x, y, z);
        final Block block = pos.getBlock(world);

        switch (getState(itemStack)) {
            case ROTATE:
                final ForgeDirection[] validRotations = block.getValidRotations(world, x, y, z);

                if (validRotations != null && validRotations.length > 0) {
                    List<ForgeDirection> validRotationsList = Arrays.asList(validRotations);
                    ForgeDirection facing = ForgeDirection.getOrientation(side);

                    if (!player.isSneaking() && validRotationsList.contains(facing)) {
                        block.rotateBlock(world, x, y, z, facing);
                    } else if (player.isSneaking() && validRotationsList.contains(facing.getOpposite())) {
                        block.rotateBlock(world, x, y, z, facing.getOpposite());
                    }
                }

                return true;
        }

        return false;
    }

    @Override
    public boolean doesSneakBypassUse(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return getState(player.getHeldItem()) == EnumWrenchState.WRENCH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canUseWrench(final EntityPlayer player, final int x, final int y, final int z) {
        return getState(player.getHeldItem()) == EnumWrenchState.WRENCH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canWrench(final EntityPlayer player, final int x, final int y, final int z) {
        return getState(player.getHeldItem()) == EnumWrenchState.WRENCH;
    }

    @Override
    public void wrenchUsed(final EntityPlayer player, final int x, final int y, final int z) {
        player.swingItem();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isUsable(final ItemStack itemStack, final EntityLivingBase entity, final int x, final int y, final int z) {
        return getState(itemStack) == EnumWrenchState.WRENCH;
    }

    @Override
    public void toolUsed(final ItemStack item, final EntityLivingBase entity, final int x, final int y, final int z) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private EnumWrenchState getState(final ItemStack itemStack) {
        return EnumWrenchState.values()[InventoryUtility.getNBTTagCompound(itemStack).getInteger("state")];
    }

    private void setState(final ItemStack itemStack, final EnumWrenchState state) {
        InventoryUtility.getNBTTagCompound(itemStack).setInteger("state", state.ordinal());
    }

    public enum EnumWrenchState {
        WRENCH(EnumColor.BRIGHT_GREEN),
        ROTATE(EnumColor.YELLOW);

        private final EnumColor color;

        EnumWrenchState(final EnumColor color) {
            this.color = color;
        }

        public String getName() {
            return LanguageUtility.transelate("tooltip." + name().toLowerCase());
        }

        public EnumColor getColor() {
            return color;
        }
    }
}