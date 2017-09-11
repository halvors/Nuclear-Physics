package org.halvors.nuclearphysics.client.render.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class OBJModelContainer {
    private static Map<String, IBakedModel> loadedModels = Maps.newHashMap();

    private final Map<String, String> textureReplacements = Maps.newHashMap();
    private final ResourceLocation model;
    private final String key;
    private final VertexFormat vertexFormat;
    private final IModelState state;
    private final boolean uvLock;

    private static int reloadCount = 0;
    private int cacheCount = 0;
    private IBakedModel cacheCopy;

    public OBJModelContainer(ResourceLocation model) {
        this.model = model;
        this.vertexFormat = DefaultVertexFormats.ITEM;
        this.state = null;
        this.uvLock = false;
        this.key = computeKey();
    }

    @SuppressWarnings("deprecation")
    public OBJModelContainer(ResourceLocation model, List<String> parts) {
        this.model = model;
        this.vertexFormat = DefaultVertexFormats.ITEM;
        this.state = new OBJState(parts, true);
        this.uvLock = false;
        this.key = computeKey();
    }

    public OBJModelContainer(OBJModelContainer handle, String texChannel, String resloc) {
        this.model = handle.model;
        this.vertexFormat = handle.vertexFormat;
        this.state = handle.state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        textureReplacements.put(texChannel, resloc);
        this.key = computeKey();
    }

    public OBJModelContainer(OBJModelContainer handle, VertexFormat fmt) {
        this.model = handle.model;
        this.vertexFormat = fmt;
        this.state = handle.state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        this.key = computeKey();
    }

    public OBJModelContainer(OBJModelContainer handle, IModelState state) {
        this.model = handle.model;
        this.vertexFormat = handle.vertexFormat;
        this.state = state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        this.key = computeKey();
    }

    public OBJModelContainer(OBJModelContainer handle, boolean uvLock) {
        this.model = handle.model;
        this.vertexFormat = handle.vertexFormat;
        this.state = handle.state;
        this.uvLock = uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        this.key = computeKey();
    }

    private String computeKey() {
        StringBuilder b = new StringBuilder();
        b.append(model.toString());

        for (Map.Entry<String, String> entry : textureReplacements.entrySet()) {
            b.append("//");
            b.append(entry.getKey());
            b.append("/");
            b.append(entry.getValue());
        }

        b.append("//VF:");
        b.append(vertexFormat.hashCode());
        b.append("//S:");
        b.append((state != null) ? state.hashCode() : "n");
        b.append("//UVL:");
        b.append(uvLock);

        return b.toString();
    }

    public OBJModelContainer replace(String texChannel, String resloc) {
        if (textureReplacements.containsKey(texChannel) && textureReplacements.get(texChannel).equals(resloc)) {
            return this;
        }

        return new OBJModelContainer(this, texChannel, resloc);
    }

    public ResourceLocation getModel()
    {
        return model;
    }

    public String getKey() {
        return key;
    }

    public Map<String, String> getTextureReplacements()
    {
        return textureReplacements;
    }

    public VertexFormat getVertexFormat()
    {
        return vertexFormat;
    }

    public OBJModelContainer setVertexFormat(VertexFormat fmt) {
        if (vertexFormat == fmt) {
            return this;
        }

        return new OBJModelContainer(this, fmt);
    }

    @Nullable
    public IModelState getState()
    {
        return state;
    }

    public OBJModelContainer setState(IModelState newState) {
        if (state == newState) {
            return this;
        }

        return new OBJModelContainer(this, newState);
    }

    public boolean isUvLocked()
    {
        return uvLock;
    }

    public OBJModelContainer setUvLocked(boolean uvLock) {
        if (this.uvLock == uvLock) {
            return this;
        }

        return new OBJModelContainer(this, uvLock);
    }

    public IBakedModel get() {
        if (cacheCount == reloadCount && cacheCopy != null) {
            return cacheCopy;
        }

        return cacheCopy = loadModel(this);
    }

    public void render()
    {
        renderModel(get(), getVertexFormat());
    }

    private static void renderModel(IBakedModel model, VertexFormat fmt) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, fmt);

        for (BakedQuad bakedquad : model.getQuads(null, null, 0)) {
            bufferBuilder.addVertexData(bakedquad.getVertexData());
        }

        tessellator.draw();
    }

    private static IBakedModel loadModel(OBJModelContainer handle) {
        IBakedModel model = loadedModels.get(handle.getKey());

        if (model != null) {
            return model;
        }

        try {
            IModel mod = ModelLoaderRegistry.getModel(handle.getModel()).process(ImmutableMap.of("flip-v", "true"));

            /*
            if (mod instanceof IRetexturableModel && handle.getTextureReplacements().size() > 0) {
                IRetexturableModel rtm = (IRetexturableModel) mod;
                mod = rtm.retexture(ImmutableMap.copyOf(handle.getTextureReplacements()));
            }

            if (handle.isUvLocked() && mod instanceof IModelUVLock) {
                IModelUVLock uvl = (IModelUVLock) mod;
                mod = uvl.uvlock(true);
            }
            */

            IModelState state = handle.getState();

            if (state == null) {
                state = mod.getDefaultState();
            }

            model = mod.bake(state, handle.getVertexFormat(), ModelLoader.defaultTextureGetter());
            loadedModels.put(handle.getKey(), model);

            return model;
        } catch (Exception e) {
            throw new ReportedException(new CrashReport("Error loading custom model " + handle.getModel(), e));
        }
    }
}