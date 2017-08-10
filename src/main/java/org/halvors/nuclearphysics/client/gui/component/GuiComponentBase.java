package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGui;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponentBase implements IGuiComponent {
	private final ResourceLocation defaultResource;

	final ResourceLocation resource;
	final IGui gui;

	GuiComponentBase(String texture, IGui gui, ResourceLocation defaultResource) {
		this.resource = ResourceUtility.getResource(ResourceType.GUI_COMPONENT, texture);
		this.gui = gui;
		this.defaultResource = defaultResource;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidthSize, int guiHeightSize) {
		RenderUtility.bindTexture(defaultResource);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int xSize, int ySize) {
		RenderUtility.bindTexture(defaultResource);
	}

	void displayTooltip(String text, int xAxis, int yAxis) {
		gui.displayTooltip(text, xAxis, yAxis);
	}

	void displayTooltips(List<String> list, int xAxis, int yAxis) {
		gui.displayTooltips(list, xAxis, yAxis);
	}

	void renderScaledText(String text, int x, int y, int color, int maxX) {
		int length = getFontRenderer().getStringWidth(text);

		if (length <= maxX) {
			getFontRenderer().drawString(text, x, y, color);
		} else {
			float scale = (float) maxX / length;
			float reverse = 1 / scale;
			float yAdd = 4 - (scale * 8) / 2F;

			GlStateManager.pushMatrix();
			GlStateManager.scale(scale, scale, scale);
			getFontRenderer().drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);
			GlStateManager.popMatrix();
		}
	}

	private FontRenderer getFontRenderer() {
		return gui.getFontRenderer();
	}
}