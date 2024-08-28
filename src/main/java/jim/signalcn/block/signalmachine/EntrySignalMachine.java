package jim.signalcn.block.signalmachine;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

public class EntrySignalMachine extends HorizontalDirectionalBlock implements IBE<EntrySignalMachineEntity>, IWrenchable {

    public static final EnumProperty<SignalType> ENTRY_TYPE = EnumProperty.create("entry_type", SignalType.class);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public enum SignalType implements StringRepresentable {
        ENTRY_SIGNAL, CROSS_SIGNAL;

        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }

    public EntrySignalMachine(BlockBehaviour.Properties properties){
        super(properties);
        registerDefaultState(defaultBlockState().setValue(ENTRY_TYPE, SignalType.ENTRY_SIGNAL)
                .setValue(POWERED, false));
    }

    @Override
    public Class<EntrySignalMachineEntity> getBlockEntityClass() {
        return EntrySignalMachineEntity.class;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(ENTRY_TYPE, POWERED));
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(POWERED, Boolean.valueOf(pContext.getLevel()
                        .hasNeighborSignal(pContext.getClickedPos())));
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos,
                                boolean pIsMoving) {
        if (pLevel.isClientSide)
            return;
        boolean powered = pState.getValue(POWERED);
        if (powered == pLevel.hasNeighborSignal(pPos))
            return;
        if (powered) {
            pLevel.scheduleTick(pPos, this, 4);
        } else {
            pLevel.setBlock(pPos, pState.cycle(POWERED), 2);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRand) {
        if (pState.getValue(POWERED) && !pLevel.hasNeighborSignal(pPos))
            pLevel.setBlock(pPos, pState.cycle(POWERED), 2);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public BlockEntityType<? extends EntrySignalMachineEntity> getBlockEntityType() {
        return BlockEntityTypes.ENTRY_SIGNAL.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        withBlockEntityDo(level, pos, ste -> {
            SignalBoundary signal = ste.getSignal();
            Player player = context.getPlayer();
            if (signal != null) {
                signal.cycleSignalType(pos);
                if (player != null)
                    player.displayClientMessage(Lang.translateDirect("track_signal.mode_change." + signal.getTypeFor(pos)
                            .getSerializedName()), true);
            } else if (player != null)
                player.displayClientMessage(Lang.translateDirect("track_signal.cannot_change_mode"), true);
        });
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }
}
