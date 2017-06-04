package org.halvors.quantum.common.updater;

import cpw.mods.fml.common.versioning.ArtifactVersion;

public class ReleaseVersion implements ArtifactVersion {
    private final String label;
    private final int major;
    private final int minor;
    private final int patch;

    public ReleaseVersion(String label, int major, int minor, int patch) {
        this.label = label;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public ReleaseVersion(String label, String s) {
        int major = 0;
        int minor = 0;
        int patch = 0;
        String[] parts = s.split("\\.");

        switch (parts.length) {
            case 3:
                patch = Integer.parseInt(parts[2]);
            case 2:
                minor = Integer.parseInt(parts[1]);
            case 1:
                major = Integer.parseInt(parts[0]);
        }

        this.label = label;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static ReleaseVersion parse(String label, String s) {
        return new ReleaseVersion(label, s);
    }

    @Override
    public int compareTo(ArtifactVersion artifactVersion) {
        if (artifactVersion instanceof ReleaseVersion) {
            return compareTo((ReleaseVersion) artifactVersion);
        }

        if (artifactVersion instanceof ModVersion) {
            ModVersion modVersion = (ModVersion) artifactVersion;

            if (label.equals(modVersion.getLabel())) {
                return compareTo(modVersion.getModVersion());
            } else if (label.equals("Minecraft")) {
                return compareTo(modVersion.getMinecraftVersion());
            }
        }

        return 0;
    }

    public int compareTo(ReleaseVersion releaseVersion) {
        if (major != releaseVersion.getMajor()) {
            return major < releaseVersion.getMajor() ? -1 : 1;
        }

        if (minor != releaseVersion.getMinor()) {
            return minor < releaseVersion.getMinor() ? -1 : 1;
        }

        if (patch != releaseVersion.getPatch()) {
            return patch < releaseVersion.getPatch() ? -1 : 1;
        }

        return 0;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getVersionString() {
        return major + "." + minor + "." + patch;
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
        return label + " " + getVersionString();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }
}
