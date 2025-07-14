package org.agent.hexvoid.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.agent.hexvoid.blocks.portal_mapper.PortalMapperBlock;
import org.agent.hexvoid.config.HexvoidConfig;
import org.agent.hexvoid.registry.HexvoidBlocks;
import org.agent.hexvoid.tags.HexvoidFluidTags;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(Entity.class)
public abstract class InLiquidQuartzDetectorMixin {
    private final Entity self = (Entity) (Object) this;
    @Shadow @Deprecated public abstract boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> fluidTag, double motionScale);

    @Shadow private Level level;
    @Shadow private BlockPos blockPosition;

    @Unique
    private final Map<BlockPos, Integer> hexVoid$checkedPositions = new HashMap<>();

    @Unique
    private static final TagKey<Fluid> hexVoid$LiquidQuartzTag = HexvoidFluidTags.Companion.getLIQUID_QUARTZ();

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void tick(CallbackInfo info) {
        if (updateFluidHeightAndDoFluidPushing(hexVoid$LiquidQuartzTag, 0.014)) {
            if (!self.canChangeDimensions() || self.isOnPortalCooldown())
                return;

            if (!this.level.isClientSide) {
                var config = HexvoidConfig.getServer();
                BlockPos pos = hexVoid$searchForPortal(self.blockPosition(), (ServerLevel)this.level,
                        0, 0, config.getPortalRecursionDepth(), config.getPortalLeapDistance());
                if (pos != null) {
                    PortalMapperBlock portal = (PortalMapperBlock) level.getBlockState(pos).getBlock();
                    portal.teleport(self, (ServerLevel)level, pos);
                }
                hexVoid$checkedPositions.clear();
            }
        }
    }

    @Unique
    private BlockPos hexVoid$searchForPortal(BlockPos pos, ServerLevel level, int depth, int jumpDepth, int MAX_DEPTH, int MAX_JUMP_DEPTH) {
        var value = hexVoid$checkedPositions.getOrDefault(pos, null);
        if (value != null) {
            if (value <= jumpDepth) {
                return null;
            }
            hexVoid$checkedPositions.put(pos, jumpDepth);
        }
        hexVoid$checkedPositions.put(pos, jumpDepth);

        BlockState state = level.getBlockState(pos);

        if (state.is(HexvoidBlocks.PORTAL_MAPPER_FULL.getBlock()))
            return pos;

        if (depth > MAX_DEPTH) {
            return null;
        }

        if (!state.is(HexvoidBlocks.LIQUID_QUARTZ_BLOCK.getValue())) {
            if (jumpDepth < MAX_JUMP_DEPTH) {
                jumpDepth++;
            }
            else {
                return null;
            }
        }

        BlockPos newPos = hexVoid$searchForPortal(pos.above(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);
        if (newPos == null) newPos = hexVoid$searchForPortal(pos.below(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);
        if (newPos == null) newPos = hexVoid$searchForPortal(pos.north(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);
        if (newPos == null) newPos = hexVoid$searchForPortal(pos.south(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);
        if (newPos == null) newPos = hexVoid$searchForPortal(pos.east(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);
        if (newPos == null) newPos = hexVoid$searchForPortal(pos.west(), level, depth+1, jumpDepth, MAX_DEPTH, MAX_JUMP_DEPTH);

        return newPos;
    }

    @Unique
    public Map<BlockPos, Integer> hexVoid$getCheckedPositions() {
        return hexVoid$checkedPositions;
    }
}
