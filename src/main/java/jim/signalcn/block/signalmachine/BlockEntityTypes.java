package jim.signalcn.block.signalmachine;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import jim.signalcn.SignalCN;
import jim.signalcn.block.ModBlocks;

public class BlockEntityTypes {
    public static final BlockEntityEntry<EntrySignalMachineEntity> ENTRY_SIGNAL = SignalCN.REGISTRATE
            .blockEntity("entry_signal", EntrySignalMachineEntity::new)
            .renderer(() -> EntrySignalMachineRenderer::new)
            .validBlocks(ModBlocks.ENTRY_SIGNAL_MACHINE)
            .register();
}
