package com.harleylizard.chunkage.task;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class SetSmallFlowerTask implements Task {
    public static final MapCodec<SetSmallFlowerTask> CODEC = MapCodec.unit(SetSmallFlowerTask::new);

    @Override
    public void apply(Level level, BlockPos blockPos, RandomSource source) {
        var blockState = Blocks.POPPY.defaultBlockState();
        if (blockState.canSurvive(level, blockPos) && level.getBlockState(blockPos).is(BlockTags.AIR)) {
            Task.setBlockFast(level, blockPos, blockState);
        }
    }

    @Override
    public TaskType<? extends Task> getTaskType() {
        return TaskType.SET_SMALL_FLOWER;
    }
}
