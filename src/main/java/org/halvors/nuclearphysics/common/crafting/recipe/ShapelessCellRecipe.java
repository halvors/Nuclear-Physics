package org.halvors.nuclearphysics.common.crafting.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.RecipeUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ShapelessCellRecipe extends ShapelessOreRecipe {
    public ShapelessCellRecipe(@Nullable final ResourceLocation group, final NonNullList<Ingredient> input, final ItemStack result) {
        super(group, input, result);
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inventoryCrafting, @Nonnull World world) {
        for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
            ItemStack slot = inventoryCrafting.getStackInSlot(i);

            if (!slot.isEmpty() && slot.getItem() instanceof ItemCell && FluidUtil.getFluidContained(slot) != null) {
                return false;
            }
        }

        return super.matches(inventoryCrafting, world);
    }

    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(final JsonContext context, final JsonObject json) {
            final String group = JsonUtils.getString(json, "group", "");
            final NonNullList<Ingredient> ingredients = RecipeUtility.parseShapeless(context, json);
            ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

            final String fluidName = JsonUtils.getJsonObject(json, "result").get("fluid").getAsString();
            final Fluid fluid = FluidRegistry.getFluid(fluidName);

            if (fluid != null) {
                final ItemStack filledCell = FluidUtility.getFilledCell(result, fluid);
            } else {
                throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
            }

            return new ShapelessCellRecipe(group.isEmpty() ? null : new ResourceLocation(group), ingredients, result);
        }
    }
}
