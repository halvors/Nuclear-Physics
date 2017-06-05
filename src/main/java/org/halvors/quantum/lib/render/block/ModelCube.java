package org.halvors.quantum.lib.render.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    public static final ModelCube INSTNACE = new ModelCube();
    private ModelRenderer cube;

    public ModelCube() {
        int size = 16;
        cube = new ModelRenderer(this, 0, 0);
        cube.addBox(-size / 2, -size / 2, -size / 2, size, size, size);
        cube.setTextureSize(112, 70);
        cube.mirror = true;
    }

    public void render() {
        cube.render(0.0625F);
    }
}
