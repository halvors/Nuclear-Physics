package org.halvors.quantum.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.client.gui.IGui;
import org.halvors.quantum.common.base.ResourceType;
import org.halvors.quantum.common.util.ResourceUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponentBase implements IGuiComponent {
	private final ResourceLocation defaultResource;

	static final Minecraft game = Minecraft.getMinecraft();

	final ResourceLocation resource;
	final IGui gui;

	GuiComponentBase(String texture, IGui gui, ResourceLocation defaultResource) {
		this.resource = ResourceUtils.getResource(ResourceType.GUI_COMPONENT, texture);
		this.gui = gui;
		this.defaultResource = defaultResource;
	}

	@Override
	public void renderBackground(int xAxis, int yAxis, int xOrigin, int yOrigin, int guiWidthSize, int guiHeightSize) {
		game.renderEngine.bindTexture(defaultResource);
	}

	@Override
	public void renderForeground(int xAxis, int yAxis, int xSize, int ySize) {
		game.renderEngine.bindTexture(defaultResource);
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

			GL11.glPushMatrix();

			GL11.glScalef(scale, scale, scale);
			getFontRenderer().drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

			GL11.glPopMatrix();
		}
	}

	private FontRenderer getFontRenderer() {
		return gui.getFontRenderer();
	}
}