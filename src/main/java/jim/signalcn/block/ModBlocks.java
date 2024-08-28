package jim.signalcn.block;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.SignalBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import com.tterrag.registrate.util.entry.BlockEntry;
import jim.signalcn.SignalCN;
import jim.signalcn.block.signalmachine.EntrySignalMachine;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;


public class ModBlocks {
    public static final BlockEntry<EntrySignalMachine> ENTRY_SIGNAL_MACHINE = SignalCN.REGISTRATE.block("entry_signal_machine", EntrySignalMachine::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.PODZOL)
                    .noOcclusion()
                    .sound(SoundType.NETHERITE_BLOCK))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.getVariantBuilder(c.get())
                    .forAllStates(state -> ConfiguredModel.builder()
                            .modelFile(AssetLookup.partialBaseModel(c, p, state.getValue(SignalBlock.TYPE)
                                    .getSerializedName()))
                            .build()))
            .item(TrackTargetingBlockItem.ofType(EdgePointType.SIGNAL))
            .transform(customItemModel())
            .register();

    public static void register() {
    }
}
