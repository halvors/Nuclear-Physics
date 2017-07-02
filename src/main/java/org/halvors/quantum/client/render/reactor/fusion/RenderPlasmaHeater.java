package org.halvors.quantum.client.render.reactor.fusion;

/*
@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends TileEntitySpecialRenderer<TilePlasmaHeater> {
    private static final IModel model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "plasma_heater.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "plasmaHeater.png");

    @Override
    public void renderTileEntityAt(TilePlasmaHeater tile, double x, double y, double z, float partialTicks, int destroyStage) {
        TilePlasmaHeater tilePlasmaHeater = (TilePlasmaHeater) tile;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        RenderUtility.bind(texture);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(tilePlasmaHeater.rotation), 0, 1, 0);
        model.renderOnly("rrot", "srot");
        GL11.glPopMatrix();

        model.renderAllExcept("rrot", "srot");
        GL11.glPopMatrix();
    }
}
*/