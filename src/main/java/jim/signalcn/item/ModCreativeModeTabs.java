package jim.signalcn.item;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import jim.signalcn.SignalCN;
import jim.signalcn.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SignalCN.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SIGNAL_COMPONENT_TAB = CREATIVE_MODE_TABS.register("signal_component_tab",
            () -> CreativeModeTab.builder()
                    .icon(makeIconSupplierItem(ModItems.RED_LAMP))
                    .title(Component.translatable("creativetab.signal_component_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.RED_LAMP.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> SIGNAL_MACHINE_TAB = CREATIVE_MODE_TABS.register("signal_machine_tab",
            () -> CreativeModeTab.builder()
                    .icon(makeIconSupplierBlock(ModBlocks.ENTRY_SIGNAL_MACHINE))
                    .title(Component.translatable("creativetab.signal_machine_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.ENTRY_SIGNAL_MACHINE.get());
                    })
                    .build());

    // Helper method to safely create a Supplier for the icon
    private static Supplier<ItemStack> makeIconSupplierItem(ItemEntry<Item> registryObject) {
        return () -> new ItemStack(registryObject.get().asItem());
    }

    private static Supplier<ItemStack> makeIconSupplierBlock(BlockEntry<?> registryObejct) {
        return () -> new ItemStack(registryObejct.get().asItem());
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
