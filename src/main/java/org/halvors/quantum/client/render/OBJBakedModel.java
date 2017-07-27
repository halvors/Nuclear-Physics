package org.halvors.quantum.client.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class OBJBakedModel {
    static Map<String, IBakedModel> loadedModels = Maps.newHashMap();

    private final Map<String, String> textureReplacements = Maps.newHashMap();
    private final ResourceLocation model;
    private final String key;
    private final VertexFormat vertexFormat;
    private final IModelState state;
    private final boolean uvLock;

    private static int reloadCount = 0;
    private int cacheCount = 0;
    private IBakedModel cacheCopy;

    public OBJBakedModel(ResourceLocation model) {
        this.model = model;
        this.vertexFormat = DefaultVertexFormats.ITEM;
        this.state = null;
        this.uvLock = false;
        this.key = computeKey();
    }

    public OBJBakedModel(ResourceLocation model, List<String> parts) {
        this.model = model;
        this.vertexFormat = DefaultVertexFormats.ITEM;
        this.state = new OBJState(parts, true);
        this.uvLock = false;
        this.key = computeKey();
    }

    public OBJBakedModel(OBJBakedModel handle, String texChannel, String resloc) {
        this.model = handle.model;
        this.vertexFormat = handle.vertexFormat;
        this.state = handle.state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        textureReplacements.put(texChannel, resloc);
        this.key = computeKey();
    }

    public OBJBakedModel(OBJBakedModel handle, VertexFormat fmt) {
        this.model = handle.model;
        this.vertexFormat = fmt;
        this.state = handle.state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        this.key = computeKey();
    }

    public OBJBakedModel(OBJBakedModel handle, IModelState state) {
        this.model = handle.model;
        this.vertexFormat = handle.vertexFormat;
        this.state = state;
        this.uvLock = handle.uvLock;
        textureReplacements.putAll(handle.textureReplacements);
        this.key = computeKey();
    }

    public OBJBakedModel(OBJBakedModel handle, boolean uvLock) {
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

    public OBJBakedModel replace(String texChannel, String resloc) {
        if (textureReplacements.containsKey(texChannel) && textureReplacements.get(texChannel).equals(resloc)) {
            return this;
        }

        return new OBJBakedModel(this, texChannel, resloc);
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

    public OBJBakedModel setVertexFormat(VertexFormat fmt) {
        if (vertexFormat == fmt) {
            return this;
        }

        return new OBJBakedModel(this, fmt);
    }

    @Nullable
    public IModelState getState()
    {
        return state;
    }

    public OBJBakedModel setState(IModelState newState) {
        if (state == newState) {
            return this;
        }

        return new OBJBakedModel(this, newState);
    }

    public boolean isUvLocked()
    {
        return uvLock;
    }

    public OBJBakedModel setUvLocked(boolean uvLock) {
        if (this.uvLock == uvLock) {
            return this;
        }

        return new OBJBakedModel(this, uvLock);
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

    // ========================================================= STATIC METHODS

    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;

        IResourceManager rm = Minecraft.getMinecraft().getResourceManager();

        if (rm instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) rm).registerReloadListener(__ -> {
                loadedModels.clear();
                reloadCount++;
            });
        }
    }

    private static void renderModel(IBakedModel model, VertexFormat fmt) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(GL11.GL_QUADS, fmt);

        for (BakedQuad bakedquad : model.getQuads(null, null, 0)) {
            vertexBuffer.addVertexData(bakedquad.getVertexData());
        }

        tessellator.draw();
    }

    private static IBakedModel loadModel(OBJBakedModel handle) {
        IBakedModel model = loadedModels.get(handle.getKey());

        if (model != null) {
            return model;
        }

        try {
            IModel mod = ((OBJModel) ModelLoaderRegistry.getModel(handle.getModel())).process(ImmutableMap.of("flip-v", "true"));

            if (mod instanceof IRetexturableModel && handle.getTextureReplacements().size() > 0) {
                IRetexturableModel rtm = (IRetexturableModel) mod;
                mod = rtm.retexture(ImmutableMap.copyOf(handle.getTextureReplacements()));
            }

            if (handle.isUvLocked() && mod instanceof IModelUVLock) {
                IModelUVLock uvl = (IModelUVLock) mod;
                mod = uvl.uvlock(true);
            }

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