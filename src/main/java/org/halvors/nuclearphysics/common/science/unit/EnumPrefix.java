package org.halvors.nuclearphysics.common.science.unit;

public enum EnumPrefix {
    //YOKTO("Yokto", "y", Math.pow(10, -24)),
    //ZEPTO("Zepto", "z", Math.pow(10, -21)),
    //ATTO("Atto", "a", Math.pow(10, -18)),
    FEMTO("Femto", "f", Math.pow(10, -15)),
    PICO("Pico", "p", Math.pow(10, -12)),
    NANO("Nano", "n", Math.pow(10, -9)),
    MICRO("Micro", "u", Math.pow(10, -6)),
    MILLI("Milli", "m", Math.pow(10, -3)),
    //CENTI("Centi", "c", Math.pow(10, -2)),
    //DESI("Desi", "d", Math.pow(10, -1)),
    NONE("", "", 1),
    //DEKA("Deka", "da", Math.pow(10, 1)),
    //HEKTO("Hekto", "h", Math.pow(10, 2)),
    KILO("Kilo", "k", Math.pow(10, 3)),
    MEGA("Mega", "M", Math.pow(10, 6)),
    GIGA("Giga", "G", Math.pow(10, 9)),
    TERA("Tera", "T", Math.pow(10, 12)),
    PETA("Peta", "P", Math.pow(10, 15)),
    EXA("Exa", "E", Math.pow(10, 18)),
    ZETTA("Zetta", "Z", Math.pow(10, 21)),
    YOTTA("Yotta", "Y", Math.pow(10, 24));

    public final String name;
    public final String symbol;
    public final double value;

    EnumPrefix(final String name, final String symbol, final double value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }

    public String getName(final boolean isShort) {
        if (isShort) {
            return symbol;
        } else {
            return name;
        }
    }

    public double process(final double d) {
        return d / value;
    }

    public boolean above(final double d) {
        return d > value;
    }

    public boolean below(final double d) {
        return d < value;
    }
}