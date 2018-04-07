package org.halvors.nuclearphysics.client.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.item.ItemCell.EnumCell;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemRenderingHandler implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType itemRenderType) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType itemRenderType, ItemStack itemStack, ItemRendererHelper itemRendererHelper) {
        return itemRendererHelper == ItemRendererHelper.ENTITY_BOBBING || itemRendererHelper == ItemRendererHelper.ENTITY_ROTATION;
    }

    @Override
    public void renderItem(ItemRenderType itemRenderType, ItemStack itemStack, Object... data) {
        Item item = itemStack.getItem();
        IIcon icon = itemStack.getIconIndex();

        // If item is cell, get texture based on fluid contained.
        if (item instanceof ItemCell) {
            FluidStack fluidStack = ((ItemCell) item).getFluid(itemStack);

            if (fluidStack != null) {
                Fluid fluid = fluidStack.getFluid();

                for (EnumCell type : EnumCell.values()) {
                    if (fluid.equals(type.getFluid())) {
                        icon = ItemCell.iconMap.get(type);
                    }
                }
            }
        }

        final Tessellator tessellator = Tessellator.instance;
        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        GL11.glPushMatrix();

        if (itemRenderType == ItemRenderType.INVENTORY) {
            GL11.glScaled(16, 16, 10);
            GL11.glTranslated(0, 1, 0);
            GL11.glRotated(180, 1, 0, 0);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            tessellator.startDrawingQuads();
            tessellator.setNormal(0, 1, 0);
            tessellator.addVertexWithUV(0, 0, 0, minU, maxV);
            tessellator.addVertexWithUV(1, 0, 0, maxU, maxV);
            tessellator.addVertexWithUV(1, 1, 0, maxU, minV);
            tessellator.addVertexWithUV(0, 1, 0, minU, minV);
            tessellator.draw();
        } else {
            if (itemRenderType != ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslated(-0.5, -0.3, 0.01);
            }

            float f12 = 0.0625F;
            ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), f12);

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(1, 1, 1, 1);
            GL11.glScaled(1, 1.1, 1);
            GL11.glTranslated(0, 1.07, f12 / -2);
            GL11.glRotatef(180, 1, 0, 0);
        }

        GL11.glColor4d(1, 1, 1, 1);

        if (itemRenderType == ItemRenderType.INVENTORY) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        } else {
            GL11.glEnable(GL11.GL_CULL_FACE);
        }

        GL11.glPopMatrix();
    }
}