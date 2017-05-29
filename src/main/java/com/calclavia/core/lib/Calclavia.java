package com.calclavia.core.lib;

import cpw.mods.fml.common.Loader;
import java.io.File;
import java.util.logging.Logger;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class Calclavia
{
    public static final String NAME = "Calclavia";
    public static final String DOMAIN = "calclavia";
    public static final String TEXTURE_NAME_PREFIX = "calclavia:";
    public static final String RESOURCE_DIRECTORY = "/assets/calclavia/";
    public static final String TEXTURE_DIRECTORY = "textures/";
    public static final String GUI_DIRECTORY = "textures/gui/";
    public static final ResourceLocation GUI_EMPTY_FILE = new ResourceLocation("calclavia", "textures/gui/gui_empty.png");
    public static final ResourceLocation GUI_BASE = new ResourceLocation("calclavia", "textures/gui/gui_base.png");
    public static final ResourceLocation GUI_COMPONENTS = new ResourceLocation("calclavia", "textures/gui/gui_components.png");
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "Calclavia.cfg"));
    public static final Logger LOGGER = Logger.getLogger("Calclavia");
}