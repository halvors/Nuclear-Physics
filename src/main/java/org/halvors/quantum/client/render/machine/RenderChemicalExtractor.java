package org.halvors.quantum.client.render.machine;

/*
@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer<TileChemicalExtractor> {
    private static final IModel model = OBJAdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "chemicalExtractor.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "chemicalExtractor.png");

    @Override
    public void renderTileEntityAt(TileChemicalExtractor tile, double x, double y, double z, float partialTicks, int destroyStage) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            if (tile.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tile.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glTranslated(0.1875, 0.4375, 0);
            GL11.glRotated(Math.toDegrees(tile.rotation), 0, 0, 1);
            GL11.glTranslated(-0.1875, -0.4375, 0);
            model.renderOnly("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();

            model.renderAllExcept("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();
        }
    }
}
*/