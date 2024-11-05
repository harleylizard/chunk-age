package com.harleylizard.chunkage.task;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public final class SetBlockTask implements Task {
    public static final MapCodec<SetBlockTask> CODEC = RecordCodecBuilder.mapCodec(builder -> {
        return builder.group(BlockStateProvider.CODEC.fieldOf("blockstate_provider").forGetter(task -> task.provider)).apply(builder, SetBlockTask::new);
    });

    private final BlockStateProvider provider;

    private SetBlockTask(BlockStateProvider provider) {
        this.provider = provider;
    }

    @Override
    public void apply(Level level, BlockPos blockPos, RandomSource source) {
        Task.setBlockFast(level, blockPos, provider.getState(source, blockPos));
    }

    @Override
    public TaskType<? extends Task> getTaskType() {
        return TaskType.SET_BLOCK;
    }
}
