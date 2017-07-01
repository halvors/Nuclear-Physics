package org.halvors.quantum.client.render.reactor.fission;

/*
@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer<TileReactorCell> {
    private static final IModel modelTop = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "reactorCellTop.obj"));
    private static final IModel modelMiddle = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "reactorCellMiddle.obj"));
    private static final IModel modelBottom = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "reactorCellBottom.obj"));
    private static final ResourceLocation textureTop = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactorCellTop.png");
    private static final ResourceLocation textureMiddle = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactorCellMiddle.png");
    private static final ResourceLocation textureBottom = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactorCellBottom.png");
    private static final ResourceLocation textureFissile = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "reactorFissileMaterial.png");

    @Override
    public void renderTileEntityAt(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        int metadata = 2;

        if (tile.getWorld() != null) {
            metadata = tile.getBlockMetadata();
        }

        boolean hasBelow = tile.getWorld() != null && tile.getWorld().getTileEntity(tile.getPos().up()) instanceof TileReactorCell;

        switch (metadata) {
            case 0:
                RenderUtility.bind(textureBottom);
                modelBottom.renderAll();
                break;

            case 1:
                RenderUtility.bind(textureMiddle);
                modelMiddle.renderAll();
                break;

            case 2:
                RenderUtility.bind(textureTop);
                GL11.glScaled(1, 1.3, 1);

                if (hasBelow) {
                    GL11.glTranslated(0, -0.125, 0);
                    modelTop.renderAllExcept("BottomPad", "BaseDepth", "BaseWidth", "Base");
                } else {
                    modelTop.renderAll();
                }
                break;
        }

        // Render fissile fuel inside reactor.
        ItemStack itemStackFuel = tile.getStackInSlot(0);

        if (itemStackFuel != null) {
            float height = tile.getHeight() * (((float) itemStackFuel.getMaxDamage() - itemStackFuel.getMetadata()) / (float) itemStackFuel.getMaxDamage());

            GL11.glPushMatrix();
            RenderUtility.bind(textureFissile);
            GL11.glScaled(0.4, 1.6 * height, 0.4);
            RenderUtility.disableLighting();
            ModelCube.instance.render();
            RenderUtility.enableLighting();
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }
}
*/
