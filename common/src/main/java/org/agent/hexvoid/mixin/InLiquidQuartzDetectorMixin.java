package org.agent.hexvoid.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.agent.hexvoid.blocks.fluids.liquid_quartz.LiquidQuartzBlock;
import org.agent.hexvoid.registry.HexvoidBlocks;
import org.agent.hexvoid.tags.HexvoidFluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class InLiquidQuartzDetectorMixin {
    private final Entity self = (Entity) (Object) this;
    @Shadow @Deprecated public abstract boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double motionScale);

    @Shadow private Level level;
    @Shadow private BlockPos blockPosition;

    @Unique
    private static final TagKey<Fluid> hexVoid$LiquidQuartzTag = HexvoidFluidTags.Companion.getLIQUID_QUARTZ();

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info) {
        if (updateFluidHeightAndDoFluidPushing(hexVoid$LiquidQuartzTag, 0.014)) {
            if (self instanceof Player && !this.level.isClientSide) { // TODO: Implement teleportation for non-player entities
                BlockState state = this.level.getBlockState(this.blockPosition);
                if (state.is(HexvoidBlocks.LIQUID_QUARTZ_BLOCK.getValue())) {
                    ((LiquidQuartzBlock) state.getBlock()).teleport((ServerPlayer)self);
                }
            }
        }
    }
}
