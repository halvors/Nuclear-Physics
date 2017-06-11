package org.halvors.quantum.common.effect.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class CustomPotionEffect extends PotionEffect {
    public CustomPotionEffect(int potionID, int duration, int amplifier)
    {
        super(potionID, duration, amplifier);
    }

    public CustomPotionEffect(Potion potion, int duration, int amplifier)
    {
        this(potion.getId(), duration, amplifier);
    }

    public CustomPotionEffect(int potionID, int duration, int amplifier, List<ItemStack> curativeItems) {
        super(potionID, duration, amplifier);

        if (curativeItems == null) {
            setCurativeItems(new ArrayList<ItemStack>());
        } else {
            setCurativeItems(curativeItems);
        }
    }
}
