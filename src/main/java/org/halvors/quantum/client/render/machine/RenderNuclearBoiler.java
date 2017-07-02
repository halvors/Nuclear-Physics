package org.halvors.quantum.client.render.machine;

/*
@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer<TileNuclearBoiler> {
    private static final IModel model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "nuclear_boiler.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "nuclearBoiler.png");

    @Override
    public void renderTileEntityAt(TileNuclearBoiler tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        if (tile.getWorld() != null) {
            RenderUtility.rotateBlockBasedOnDirection(tile.getDirection());
        }

        RenderUtility.bind(texture);

        GL11.glPushMatrix();
        GL11.glTranslated(0.312958, 0, 0.187042);
        GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.312958, 0, -0.187042);
        model.renderOnly("FuelBarSupport1Rotates", "FuelBar1Rotates");
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(0.312958, 0, -0.187042);
        GL11.glRotated(-Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.312958, 0, 0.187042);
        model.renderOnly("FuelBarSupport2Rotates", "FuelBar2Rotates");
        GL11.glPopMatrix();

        model.renderAllExcept("FuelBarSupport1Rotates", "FuelBarSupport2Rotates", "FuelBar1Rotates", "FuelBar2Rotates");
        GL11.glPopMatrix();
    }
}
*/