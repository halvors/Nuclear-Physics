package org.halvors.quantum.core.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.halvors.quantum.atomic.common.block.BlockQuantum;
import org.halvors.quantum.atomic.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.atomic.common.item.block.ItemBlockTooltip;

import java.util.HashSet;
import java.util.Set;

public class QuantumComponentsBlocks {
    //public static BlockQuantum blockOreCopper;
    //public static BlockQuantum blockOreTin;
    public static BlockQuantum blockCreativeBuilder = new BlockCreativeBuilder();

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

        /**
         * Register this mod's {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            final Block[] blocks = {
                    //blockOreCopper,
                    //blockOreTin,
                    blockCreativeBuilder
            };

            registry.registerAll(blocks);
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
            final ItemBlock[] items = {
                    //new ItemBlockTooltip(blockOreCopper),
                    //new ItemBlockTooltip(blockOreTin),
                    new ItemBlockTooltip(blockCreativeBuilder)
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                final BlockQuantum block = (BlockQuantum) item.getBlock();
                //final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
                //registry.register(item.setRegistryName(registryName));
                registry.register(item);

                block.registerItemModel(item);
                block.registerBlockModel();

                ITEM_BLOCKS.add(item);
            }
        }
    }
}
