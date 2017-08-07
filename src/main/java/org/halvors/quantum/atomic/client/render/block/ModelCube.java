package org.halvors.quantum.atomic.client.render.block;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCube extends ModelBase {
    private static final ModelCube instance = new ModelCube();
    private static ModelRenderer cube;

    public ModelCube() {
        int size = 16;
        cube = new ModelRenderer(this, 0, 0);
        cube.addBox(-size / 2, -size / 2, -size / 2, size, size, size);
        cube.setTextureSize(112, 70);
        cube.mirror = true;
    }

    public static ModelCube getInstance() {
        return instance;
    }

    public void render() {
        cube.render(0.0625F);
    }
}
