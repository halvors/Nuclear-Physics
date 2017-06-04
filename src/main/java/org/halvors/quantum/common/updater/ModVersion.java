package org.halvors.quantum.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ModVersion implements ArtifactVersion {
    private final String label;
    private final ReleaseVersion minecraftVersion;
    private final ReleaseVersion modVersion;
    private final String description;

    public ModVersion(String label, ReleaseVersion minecraftVersion, ReleaseVersion modVersion, String description) {
        this.label = label;
        this.minecraftVersion = minecraftVersion;
        this.modVersion = modVersion;
        this.description = description;
    }

    public ModVersion(String label, String s) {
        String[] parts = s.split(" ", 2);
        String description = null;

        if (parts.length > 1) {
            description = parts[1].trim();
        }

        parts = parts[0].split("-", 2);

        this.label = label;
        this.minecraftVersion = new ReleaseVersion("Minecraft", parts[0]);
        this.modVersion = new ReleaseVersion(label, parts[1]);
        this.description = description;
    }

    public static ModVersion parse(String label, String s) {
        if (s == null || s.length() == 0) {
            return null;
        }

        return new ModVersion(label, s);
    }

    @Override
    public int compareTo(ArtifactVersion artifactVersion) {
        if (artifactVersion instanceof ModVersion) {
            return compareTo((ModVersion) artifactVersion);
        }

        if (artifactVersion instanceof ReleaseVersion) {
            ReleaseVersion releaseVersion = (ReleaseVersion) artifactVersion;

            if (label.equals(releaseVersion.getLabel())) {
                return modVersion.compareTo(releaseVersion);
            } else if ("Minecraft".equals(releaseVersion.getLabel())) {
                return minecraftVersion.compareTo(releaseVersion);
            }
        }

        return 0;
    }

    public int compareTo(ModVersion modVersion) {
        if (minecraftVersion.compareTo(modVersion.getMinecraftVersion()) != 0) {
            return minecraftVersion.compareTo(modVersion.getMinecraftVersion());
        }

        return this.modVersion.compareTo(modVersion.getModVersion());
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        return minecraftVersion.getVersionString() + "-" + modVersion.getVersionString();
    }

    @Override
    public boolean containsVersion(ArtifactVersion artifactVersion) {
        return compareTo(artifactVersion) == 0;
    }

    @Override
    public String getRangeString() {
        return null;
    }

    @Override
    public String toString() {
        return modVersion.toString() + " for " + minecraftVersion.toString();
    }

    public ReleaseVersion getMinecraftVersion() {
        return minecraftVersion;
    }

    public ReleaseVersion getModVersion() {
        return modVersion;
    }

    public String getDescription() {
        return description;
    }
}