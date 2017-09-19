package org.halvors.nuclearphysics.common.item.tool;

import mekanism.api.IMekWrench;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.item.IWrench;
import org.halvors.nuclearphysics.common.Integration;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nullable;
import java.util.List;

/*
@InterfaceList({
        @Interface(iface = "mekanism.api.IMekWrench", modid = Integration.MEKANISM_MOD_ID)
})
public class ItemWrench extends ItemBase { //implements IMekWrench {
    public ItemWrench(String name) {
        super(name);

        setMaxStackSize(1);
    }
}

@Override
public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
    final IBlockState state = world.getBlockState(pos);
    final Block block = state.getBlock();

    EnumFacing[] validRotations = block.getValidRotations(world, pos);

    if (validRotations != null && validRotations.length > 0) {
        block.rotateBlock(world, pos, side);

        return EnumActionResult.SUCCESS;
    }

    return EnumActionResult.PASS;
}
*/

@InterfaceList({
        @Interface(iface = "mekanism.api.IMekWrench", modid = Integration.MEKANISM_MOD_ID)
})
public class ItemWrench extends ItemBase implements IWrench, IMekWrench {
    public ItemWrench() {
        super("wrench");

        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> list, ITooltipFlag flag) {
        list.add(LanguageUtility.transelate("tooltip.state") + ": " + getState(itemStack).getName());

        super.addInformation(itemStack, world, list, flag);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        final ItemStack itemStack = player.getHeldItemMainhand();

        if (player.isSneaking()) {
            WrenchState state = getState(itemStack);
            int toSet = state.ordinal() < WrenchState.values().length - 1 ? state.ordinal() + 1 : 0;
            setState(itemStack, WrenchState.values()[toSet]);
            state = getState(itemStack);

            if (!world.isRemote) {
                player.sendMessage(new TextComponentString(Color.DARK_BLUE + "[" + Reference.NAME + "] " + Color.GREY + LanguageUtility.transelate("tooltip.state") + ": " + state.getColor() + state.getName()));

                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack itemStack = player.getHeldItemMainhand();
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();

        switch (getState(itemStack)) {
            case ROTATE:
                EnumFacing[] validRotations = block.getValidRotations(world, pos);

                if (validRotations != null && validRotations.length > 0) {
                    block.rotateBlock(world, pos, facing);

                    return EnumActionResult.SUCCESS;
                }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack itemStack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return getState(itemStack) == WrenchState.WRENCH;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer player, BlockPos pos) {
        return getState(itemStack) == WrenchState.WRENCH;
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