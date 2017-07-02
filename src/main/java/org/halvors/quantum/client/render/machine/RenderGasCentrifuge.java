package org.halvors.quantum.client.render.machine;

/*
@SideOnly(Side.CLIENT)
public class RenderGasCentrifuge extends TileEntitySpecialRenderer<TileGasCentrifuge> {
    private static final IModel model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "gas_centrifuge.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "centrifuge.png");

    @Override
    public void renderTileEntityAt(TileGasCentrifuge tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y, z + 0.5);

        if (tile.getWorld() != null) {
            RenderUtility.rotateBlockBasedOnDirection(tile.getDirection());
        }

        RenderUtility.bind(texture);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
        model.renderOnly("C", "JROT", "KROT", "LROT", "MROT");
        GL11.glPopMatrix();

        model.renderAllExcept("C", "JROT", "KROT", "LROT", "MROT");
        GL11.glPopMatrix();
    }
}
*/