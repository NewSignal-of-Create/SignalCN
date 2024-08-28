package jim.signalcn.block.signalmachine;

import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import com.simibubi.create.content.trains.signal.SignalBoundary;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class EntrySignalMachineEntity extends SmartBlockEntity implements ITransformableBlockEntity {
    public static enum OverlayState {
        RENDER, SKIP, DUAL
    }
    public static enum SignalState {
        RED, YELLOW, GREEN, INVALID;

        public boolean isRedLight(float renderTime) {
            return this == RED || this == INVALID && renderTime % 40 < 3;
        }

        public boolean isYellowLight(float renderTime) {
            return this == YELLOW;
        }

        public boolean isGreenLight(float renderTime) {
            return this == GREEN;
        }
    }

    public TrackTargetingBehaviour<SignalBoundary> edgePoint;

    private SignalState state;
    private OverlayState overlay;
    private int switchToRedAfterTrainEntered;
    private boolean lastReportedPower;

    public EntrySignalMachineEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.state = SignalState.INVALID;
        this.overlay = OverlayState.SKIP;
        this.lastReportedPower = false;
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        NBTHelper.writeEnum(tag, "State", state);
        NBTHelper.writeEnum(tag, "Overlay", overlay);
        tag.putBoolean("Power", lastReportedPower);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        state = NBTHelper.readEnum(tag, "State", SignalState.class);
        overlay = NBTHelper.readEnum(tag, "Overlay", OverlayState.class);
        lastReportedPower = tag.getBoolean("Power");
        invalidateRenderBoundingBox();
    }

    @Nullable
    public SignalBoundary getSignal() {
        return edgePoint.getEdgePoint();
    }

    public boolean isPowered() {
        return state == SignalState.RED;
    }


    public SignalState getState() {
        return state;
    }

    public OverlayState getOverlay() {
        return overlay;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        edgePoint = new TrackTargetingBehaviour<>(this, EdgePointType.SIGNAL);
        behaviours.add(edgePoint);
    }

    public boolean getReportedPower() {
        return lastReportedPower;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition, edgePoint.getGlobalPosition()).inflate(2);
    }

    @Override
    public void transform(StructureTransform transform) {
        edgePoint.transform(transform);
    }
}
