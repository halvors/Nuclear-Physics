package org.halvors.quantum.atomic.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.gui.component.IGuiComponent;
import org.halvors.quantum.atomic.common.tile.component.IComponent;
import org.halvors.quantum.atomic.common.tile.component.IComponentContainer;
import org.halvors.quantum.atomic.common.utility.ResourceUtility;
import org.halvors.quantum.atomic.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiComponentContainerScreen extends GuiScreen implements IComponentContainer, IGui {
	private static final Minecraft game = Minecraft.getMinecraft();

    protected final ResourceLocation defaultResource = ResourceUtility.getResource(ResourceType.GUI, "Screen.png");
    protected final TileEntity tileEntity;

	// This is not present by default in GuiComponentContainerScreen as it is in GuiComponentContainerInventoryScreen.
	protected final int xSize = 176;
    protected final int ySize = 100;

	private int guiLeft;
	private int guiTop;

    protected GuiComponentContainerScreen(TileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void initGui() {
		super.initGui();

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {
		drawDefaultBackground();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawGuiScreenBackgroundLayer(partialTick, mouseX, mouseY);

		super.drawScreen(mouseX, mouseY, partialTick);

		GL11.glTranslatef(guiLeft, guiTop, 0);

		drawGuiScreenForegroundLayer(mouseX, mouseY);
	}

	public float getNeededScale(String text, int maxX) {
		int length = fontRendererObj.getStringWidth(text);

		if (length <= maxX) {
			return 1;
		} else {
			return (float) maxX / length;
		}
	}

	public void renderScaledText(String text, int x, int y, int color, int maxX) {
		int length = fontRendererObj.getStringWidth(text);

		if (length <= maxX) {
			fontRendererObj.drawString(text, x, y, color);
		} else {
			float scale = (float) maxX / length;
			float reverse = 1 / scale;
			float yAdd = 4 - (scale * 8) / 2F;

			GL11.glPushMatrix();

			GL11.glScalef(scale, scale, scale);
			fontRendererObj.drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

			GL11.glPopMatrix();
		}
	}

    protected void drawGuiScreenBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        game.renderEngine.bindTexture(defaultResource);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        for (IComponent component : components) {
            if (component instanceof IGuiComponent) {
                IGuiComponent guiComponent = (IGuiComponent) component;
                guiComponent.renderBackground(xAxis, yAxis, guiWidth, guiHeight, xSize, ySize);
            }
        }
    }

	protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		drawString(tileEntity.getBlockType().getLocalizedName(), (xSize / 2) - (fontRendererObj.getStringWidth(tileEntity.getBlockType().getLocalizedName()) / 2), 6);

		int xAxis = mouseX - guiWidth;
		int yAxis = mouseY - guiHeight;

		for (IComponent component : components) {
			if (component instanceof IGuiComponent) {
				IGuiComponent guiComponent = (IGuiComponent) component;
				guiComponent.renderForeground(xAxis, yAxis, xSize, ySize);
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;
		int xAxis = mouseX - guiWidth;
		int yAxis = mouseY - guiHeight;

		for (IComponent component : components) {
			if (component instanceof IGuiComponent) {
				IGuiComponent guiComponent = (IGuiComponent) component;
				guiComponent.preMouseClicked(xAxis, yAxis, xSize, ySize, button);
			}
		}

		super.mouseClicked(mouseX, mouseY, button);

		for (IComponent component : components) {
			if (component instanceof IGuiComponent) {
				IGuiComponent guiComponent = (IGuiComponent) component;
				guiComponent.mouseClicked(xAxis, yAxis, xSize, ySize, button);
			}
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {
		super.mouseClickMove(mouseX, mouseY, button, ticks);

		int xAxis = (mouseX - (width - xSize) / 2);
		int yAxis = (mouseY - (height - ySize) / 2);

		for (IComponent component : components) {
			if (component instanceof IGuiComponent) {
				IGuiComponent guiComponent = (IGuiComponent) component;
				guiComponent.mouseClickMove(xAxis, yAxis, button, ticks);
			}
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int type) {
		super.mouseReleased(mouseX, mouseY, type);

		int xAxis = (mouseX - (width - xSize) / 2);
		int yAxis = (mouseY - (height - ySize) / 2);

		for (IComponent component : components) {
			if (component instanceof IGuiComponent) {
				IGuiComponent guiComponent = (IGuiComponent) component;
				guiComponent.mouseReleased(xAxis, yAxis, type);
			}
		}
	}

	@Override
	public void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height) {
		super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
	}

	/*
	@Override
	public void drawTexturedRectFromIcon(int x, int y, IIcon icon, int width, int height) {
		super.drawTexturedModelRectFromIcon(x, y, icon, width, height);
	}
	*/

	@Override
	public void drawString(String text, int x, int y) {
		fontRendererObj.drawString(text, x, y, 0x404040);
	}

	@Override
	public void displayTooltip(String text, int x, int y) {
		drawCreativeTabHoveringText(text, x, y);
	}

	@Override
	public void displayTooltips(List<String> list, int xAxis, int yAxis) {
		drawHoveringText(list, xAxis, yAxis);
	}

	@Override
	public FontRenderer getFontRenderer() {
		return fontRendererObj;
	}
}