package org.halvors.nuclearphysics.common.effect.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CustomPotionEffect extends PotionEffect {
    public CustomPotionEffect(Potion potion, int duration, int amplifier) {
        super(potion, duration, amplifier);
    }

    public CustomPotionEffect(Potion potion, int duration, int amplifier, List<ItemStack> curativeItems) {
        super(potion, duration, amplifier);
    }
}
