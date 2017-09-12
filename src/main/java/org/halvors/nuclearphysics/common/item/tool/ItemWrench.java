package org.halvors.nuclearphysics.common.item.tool;

import mekanism.api.IMekWrench;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import org.halvors.nuclearphysics.common.Integration;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.type.Color;

import javax.annotation.Nonnull;
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
public class ItemWrench extends ItemBase implements IMekWrench {
    public ItemWrench() {
        super("wrench");

        setMaxStackSize(1);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean flag) {
        list.add(LanguageUtility.transelate("tooltip.mode") + ": " + getState(itemStack).getName());

        super.addInformation(itemStack, player, list, flag);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
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
    public EnumActionResult onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        final IBlockState state = world.getBlockState(pos);
        final Block block = state.getBlock();

        switch (getState(itemStack)) {
            case ROTATE:
                if (player.isSneaking()) {
                    side = side.getOpposite();
                }

                EnumFacing[] validRotations = block.getValidRotations(world, pos);

                if (validRotations != null && validRotations.length > 0) {
                    block.rotateBlock(world, pos, side);

                    return EnumActionResult.SUCCESS;
                }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack itemStack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return getState(itemStack) == WrenchState.WRENCH;
    }

    public WrenchState getState(ItemStack itemStack) {
        return WrenchState.values()[InventoryUtility.getNBTTagCompound(itemStack).getInteger("state")];
    }

    public void setState(ItemStack itemStack, WrenchState state) {
        InventoryUtility.getNBTTagCompound(itemStack).setInteger("state", state.ordinal());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Mekanism
    @Override
    public boolean canUseWrench(ItemStack itemStack, EntityPlayer player, BlockPos pos) {
        return getState(itemStack) == WrenchState.WRENCH;
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
            return LanguageUtility.transelate("tooltip.wrench." + name);
        }

        public Color getColor() {
            return color;
        }
    }
}