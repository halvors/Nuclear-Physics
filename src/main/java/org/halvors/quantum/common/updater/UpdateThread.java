package org.halvors.quantum.common.updater;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.IUpdatableMod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateThread extends Thread {
    private final IUpdatableMod mod;
    private final String releaseUrl, downloadUrl;

    private ModVersion newModVersion;
    private boolean isCheckCompleted;
    private boolean isNewVersionAvailable;
    private boolean isCriticalUpdate;

    public UpdateThread(IUpdatableMod mod, String releaseUrl, String downloadUrl) {
        super(mod.getModId() + " updater");

        this.mod = mod;
        this.releaseUrl = releaseUrl;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public void run() {
        try {
            // This is our current locally used version.
            ModVersion ourVersion = ModVersion.parse(mod.getModName(), MinecraftForge.MC_VERSION + "-" + mod.getModVersion());

            // Fetch the new version from the internet.
            URL versionFile = new URL(releaseUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(versionFile.openStream()));
            newModVersion = ModVersion.parse(mod.getModName(), reader.readLine());
            ModVersion criticalVersion = ModVersion.parse(mod.getModName(), reader.readLine());
            reader.close();

            isNewVersionAvailable = ourVersion.compareTo(newModVersion) < 0;

            if (isNewVersionAvailable) {
                Quantum.getLogger().info("An updated version of " + mod.getModName() + " is available: " + newModVersion + ".");

                if (ourVersion.getMinecraftVersion().compareTo(newModVersion.getMinecraftVersion()) < 0) {
                    ReleaseVersion newReleaseVersion = newModVersion.getMinecraftVersion();
                    ReleaseVersion ourReleaseVersion = ourVersion.getMinecraftVersion();
                    isNewVersionAvailable = newReleaseVersion.getMajor() == ourReleaseVersion.getMajor() && newReleaseVersion.getMinor() == ourReleaseVersion.getMinor();
                }

                if (criticalVersion != null && ourVersion.compareTo(criticalVersion) >= 0) {
                    isCriticalUpdate = Boolean.parseBoolean(criticalVersion.getDescription());
                    isCriticalUpdate &= isNewVersionAvailable;
                }
            }

            if (isCriticalUpdate) {
                Quantum.getLogger().info("This update has been marked as CRITICAL and will ignore notification suppression.");
            }

            // VersionChecker integration.
            if (Integration.isVersionCheckerEnabled) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setString("modDisplayName", mod.getModName());
                nbtTagCompound.setString("oldVersion", ourVersion.toString());
                nbtTagCompound.setString("newVersion", newModVersion.toString());

                if (downloadUrl != null) {
                    nbtTagCompound.setString("updateUrl", downloadUrl);
                    nbtTagCompound.setBoolean("isDirectLink", false);
                }

                FMLInterModComms.sendRuntimeMessage(mod.getModId(), "VersionChecker", "addUpdate", nbtTagCompound);
                isNewVersionAvailable &= isCriticalUpdate;
            }
        } catch (Exception e) {
            Quantum.getLogger().log(Level.WARN, AbstractLogger.CATCHING_MARKER, "Update check for " + mod.getModName() + " failed.", e);
        }

        isCheckCompleted = true;
    }

    public boolean isCheckCompleted() {
        return isCheckCompleted;
    }

    public boolean isCriticalUpdate() {
        return isCriticalUpdate;
    }

    public boolean isNewVersionAvailable() {
        return isNewVersionAvailable;
    }

    public ModVersion getNewModVersion() {
        return newModVersion;
    }
}
