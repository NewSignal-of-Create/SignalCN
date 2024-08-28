package jim.signalcn.item;

import jim.signalcn.SignalCN;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class ModItems {

    // Register items using Registrate
    public static final ItemEntry<Item> RED_LAMP = SignalCN.REGISTRATE.item("red_lamp", Item::new)
            .properties(p -> p.stacksTo(64))  // Customize item properties
            .register();
    public static void register() {
        // This method is used to initialize static references.
        // No further action is needed here since Registrate automatically handles registration.
    }
}