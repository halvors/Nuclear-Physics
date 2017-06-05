package org.halvors.quantum.lib.science;

public class HeatingData {
    public float meltingPoint;
    public float boilingPoint;
    public float latentFusionHeat;
    public float latentVaporizationHeat;
    public float specificHeat;
    public float thermalExpasion;
    public float thermalConductivity;

    public HeatingData(float meltingPoint, float boilingPoint, float fisionHeat, float vaporHeat, float specificHeat) {
        this.meltingPoint = meltingPoint;
        this.boilingPoint = boilingPoint;
        this.latentFusionHeat = fisionHeat;
        this.latentVaporizationHeat = vaporHeat;
        this.specificHeat = specificHeat;
    }

    public HeatingData(float meltingPoint, float boilingPoint, float fisionHeat, float vaporHeat, float specificHeat, float thermalExpansion, float thermalConductivity) {
        this(meltingPoint, boilingPoint, fisionHeat, vaporHeat, specificHeat);
        this.thermalConductivity = thermalConductivity;
        this.thermalExpasion = thermalExpansion;
    }
}