package org.halvors.quantum.common;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.halvors.quantum.common.item.ItemQuantum;

public class QuantumItems {
    public static void register() {

    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemQuantum) {
            ((ItemQuantum) item).registerItemModel();
        }

        // TODO: Handle itemblocks?

        return item;
    }
}
