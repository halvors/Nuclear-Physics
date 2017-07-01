package org.halvors.quantum.client.render.machine;

/*
@SideOnly(Side.CLIENT)
public class RenderQuantumAssembler extends TileEntitySpecialRenderer<TileQuantumAssembler> {
    private static final IModel model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "quantumAssembler.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "quantumAssembler.png");

    @Override
    public void renderTileEntityAt(TileQuantumAssembler tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y, z + 0.5);

        String[] hands = new String[] {"Back Arm Upper", "Back Arm Lower", "Right Arm Upper", "Right Arm Lower", "Front Arm Upper", "Front Arm Lower", "Left Arm Upper", "Left Arm Lower"};
        String[] arms = new String[] {"Middle Rotor Focus Lazer", "Middle Rotor Uppper Arm", "Middle Rotor Lower Arm", "Middle Rotor Arm Base", "Middle Rotor"};
        String[] largeArms = new String[] {"Bottom Rotor Upper Arm", "Bottom Rotor Lower Arm", "Bottom Rotor Arm Base", "Bottom Rotor", "Bottom Rotor Resonator Arm"};

        RenderUtility.bind(texture);

        GL11.glPushMatrix();
        GL11.glRotated(-tile.rotationYaw1, 0, 1, 0);
        model.renderOnly(hands);
        model.renderOnly("Resonance_Crystal");
        GL11.glPopMatrix();

        // Small Laser Arm.
        GL11.glPushMatrix();
        GL11.glRotated(tile.rotationYaw2, 0, 1, 0);
        model.renderOnly(arms);

        GL11.glPopMatrix();

        // Large Laser Arm.
        GL11.glPushMatrix();
        GL11.glRotated(-tile.rotationYaw3, 0, 1, 0);
        model.renderOnly(largeArms);
        GL11.glPopMatrix();

        model.renderAllExcept(ArrayUtils.add(ArrayUtils.addAll(ArrayUtils.addAll(hands, arms), largeArms), "Resonance_Crystal"));
        GL11.glPopMatrix();

        // Render the item.
        RenderItem renderItem = ((RenderItem) RenderManager.instance.getEntityClassRenderObject(EntityItem.class));

        GL11.glPushMatrix();

        if (tile.entityItem != null) {
            renderItem.doRender(tileQuantumAssembler.entityItem, x + 0.5, y + 0.4, z + 0.5, 0, 0);
        }

        GL11.glPopMatrix();
    }
}
*/